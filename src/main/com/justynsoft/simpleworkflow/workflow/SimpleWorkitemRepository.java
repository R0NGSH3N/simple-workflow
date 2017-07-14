package com.justynsoft.simpleworkflow.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SimpleWorkitemRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_WORKITEM =
            "INSERT INTO workitem(workflow_id, workitem_template_id, status, create_datetime, lastupdate_datetime" +
                    " VALUES(?,?,?,?)";
    public void insert(SimpleWorkitem simpleWorkitem){
        jdbcTemplate.update( INSERT_WORKITEM, new Object[]{
                simpleWorkitem.getWorkflowId(),
                simpleWorkitem.getWorkitemTemplate(),
                simpleWorkitem.getStatus(),
                simpleWorkitem.getCreateDate() == null ? new Date() : simpleWorkitem.getCreateDate(),
                simpleWorkitem.getLastUpdateDateTime() == null ? new Date(): simpleWorkitem.getLastUpdateDateTime()
                }
        );
    }

    private static final String FIND_BY_WORKFLOW_ID = "SELECT * FROM workitem WHERE workflow_id = ? ";
    public List<SimpleWorkitem> findByWorkflowId(Long workflowId){
        return jdbcTemplate.query(FIND_BY_WORKFLOW_ID, new Object[]{workflowId}, new RowMapper<SimpleWorkitem>() {
            @Override
            public SimpleWorkitem mapRow(ResultSet resultSet, int i) throws SQLException {
                SimpleWorkitem simpleWorkitem = new SimpleWorkitem() {
                    @Override
                    public STATUS handleEvent(SimpleWorkflowEvent simpleWorkflowEvent, String errorMessage, SimpleWorkflow simpleWorkflow) {
                        return null;
                    }

                    @Override
                    public STATUS handleRejectEventOnStartedStatus(SimpleWorkflowEvent simpleWorkflowEvent) {
                        return null;
                    }

                    @Override
                    public void destroyWorkitem() {

                    }
                };
                simpleWorkitem.setWorkflowId(resultSet.getLong("workflow_id"));
                simpleWorkitem.setWorkitemId(resultSet.getLong("workitem_id"));
                simpleWorkitem.setStatus(SimpleWorkitem.STATUS.valueOf(resultSet.getString("status")));
                simpleWorkitem.setCreateDate(resultSet.getDate("create_datetime"));
                simpleWorkitem.setLastUpdateDateTime(resultSet.getDate("lastupdate_datetime"));
                return simpleWorkitem;
            }
        });

    }

}
