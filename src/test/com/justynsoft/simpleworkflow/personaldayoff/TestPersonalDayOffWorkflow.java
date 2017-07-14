package com.justynsoft.simpleworkflow.personaldayoff;

import com.justynsoft.simpleworkflow.workflow.SimpleWorkflow;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowEvent;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowManager;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkitem;
import org.h2.tools.DeleteDbFiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestPersonalDayOffWorkflow implements ApplicationEventPublisherAware{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleWorkflowManager workflowManager;
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Before
    public void setUp() throws Exception {
        //delete test.mv
        DeleteDbFiles.execute("~", "test", true);
        //create new workflow and insert into database

    }

    private static final String WORKITEM_INSERT = "INSERT INTO workitem_setup(class_name, is_rerunnable,name,workflow_template_id,next_workitem_template_id) " +
                " VALUES(?,?,?,?,?)";
    private void insertWorkitem(Long workflowTemplateId){
        //insert last workitem first
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(WORKITEM_INSERT, new String[] {"template_id"});
                        ps.setString(1,"com.justynsoft.simpleworkflow.personaldayoff.HumanResourceWorkdaysBackupWI");
                        ps.setBoolean(2, false);
                        ps.setString(3, "HRBackup");
                        ps.setLong(4,workflowTemplateId);
                        ps.setLong(5, Types.NULL);
                        return ps;
                    }
                },keyHolder);
        final Long lastWorkitemId = keyHolder.getKey().longValue();
        //update second
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(WORKITEM_INSERT, new String[] {"template_id"});
                        ps.setString(1,"com.justynsoft.simpleworkflow.personaldayoff.DirectivesApproveWI");
                        ps.setBoolean(2, false);
                        ps.setString(3, "DirectiveApprove");
                        ps.setLong(4,workflowTemplateId);
                        ps.setLong(5,lastWorkitemId);
                        return ps;
                    }
                },keyHolder);
        final Long secondWorkitemId = keyHolder.getKey().longValue();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(WORKITEM_INSERT, new String[] {"template_id"});
                        ps.setString(1,"com.justynsoft.simpleworkflow.personaldayoff.SendDayOffRequestWI");
                        ps.setBoolean(2, false);
                        ps.setString(3, "SendRequest");
                        ps.setLong(4,workflowTemplateId);
                        ps.setLong(5,secondWorkitemId);
                        return ps;
                    }
                },keyHolder);
    }


    private static final String PERSONAL_DAYOFF_WORKFLOW_INSERT = "INSERT INTO workflow_setup(create_datetime, name, description) " +
            "VALUES(CURRENT_DATE(), 'Peronal Day Off', 'This is workflow for employee to apply a personal day off')";
    private Long insertWorkflowTemplate(){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(PERSONAL_DAYOFF_WORKFLOW_INSERT, new String[] {"template_id"});
                        return ps;
                    }
                },keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Test
    public void runWorkflow(){
        //create workflow and workitem template
        Long workflowTemplateId = insertWorkflowTemplate();
        insertWorkitem(workflowTemplateId);

        //because workflow manager's cache doesn't have this new workflow template, so we need to refresh the cache
        workflowManager.loadTemplates();

        //start new day off workflow
        DayOffWorkflowEvent newWorkflowEvent = new DayOffWorkflowEvent(this);
        newWorkflowEvent.setType(SimpleWorkflowEvent.InternalEvent.RUN_WORKFLOW);
        newWorkflowEvent.setWorkflowTemplateId(workflowTemplateId);
        DayOffMessage message = new DayOffMessage();
        message.setFlowMsg(new ArrayList<String>());
        newWorkflowEvent.setAttachecObject(message);
        publisher.publishEvent(newWorkflowEvent);

        //when use H2 testing, all the rows are deleted after test, so the new workflow id always
        //start with 1, if you use different database, can't hack like this.
        SimpleWorkflow workflow = workflowManager.getWorkflow(1L);
        assertTrue(workflow.getStatus() == SimpleWorkflow.STATUS.INPROGRESS);

        /**
         * after new day off request sent out, the "Send Day Off request" workitem should be
         * completed, and wait for directive to approve, so the "Directive Approve Workitem" should
         * be in progress, the HR backup workitem should be in pending.
         */
        List<SimpleWorkitem> workitemList = workflow.getWorkItemList();
        workitemList.forEach(detailWorkitem -> {
            if(detailWorkitem instanceof SendDayOffRequestWI){
                assertTrue(detailWorkitem.getStatus() == SimpleWorkitem.STATUS.COMPLETED);
            }else if(detailWorkitem instanceof DirectivesApproveWI){
                assertTrue(detailWorkitem.getStatus() == SimpleWorkitem.STATUS.INPROGRESS);
            }else if(detailWorkitem instanceof HumanResourceWorkdaysBackupWI){
                assertTrue(detailWorkitem.getStatus() == SimpleWorkitem.STATUS.PENDING);
            }
        });

        //send approve event to workflow to trigger the "Directive Approve Workitem" proceed to completed
        newWorkflowEvent.setWorkflowId(1L);
        newWorkflowEvent.setType(DayOffWorkflowEvent.DayOffWorkflowEventType.REQUEST_APPROVED);
        publisher.publishEvent(newWorkflowEvent);

        /**
         * after director approve the day off request, this "Directive Approve" workitem should be completed
         * the "Human Resource Backup" workitem will be completed as well.
         */
        workflow = workflowManager.getWorkflow(1L);
        assertTrue(workflow.getStatus() == SimpleWorkflow.STATUS.COMPLETED);
        workitemList = workflow.getWorkItemList();
        workitemList.forEach(detailWorkitem -> {
            if(detailWorkitem instanceof SendDayOffRequestWI){
                assertTrue(detailWorkitem.getStatus() == SimpleWorkitem.STATUS.COMPLETED);
            }else if(detailWorkitem instanceof DirectivesApproveWI){
                assertTrue(detailWorkitem.getStatus() == SimpleWorkitem.STATUS.COMPLETED);
            }else if(detailWorkitem instanceof HumanResourceWorkdaysBackupWI){
                assertTrue(detailWorkitem.getStatus() == SimpleWorkitem.STATUS.COMPLETED);
            }
        });
    }

}
