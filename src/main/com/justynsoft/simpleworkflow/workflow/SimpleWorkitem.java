package com.justynsoft.simpleworkflow.workflow;

import com.justynsoft.simpleworkflow.template.WorkitemTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "workitem")
public abstract class SimpleWorkitem {
    private static final Logger logger = LoggerFactory.getLogger(SimpleWorkitem.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "workitem_id")
    private Long workitemId;
    @NotNull
    @Column(name = "workflow_id")
    private Long workflowId;
    @NotNull
    @Column(name = "workitem_template_id")
    private Long workitemTemplateId;
    @NotNull
    @Column(name = "status")
    private STATUS status;
    @Column(name = "create_datetime")
    private Date createDate;
    @Column(name = "lastupdate_datetime")
    private Date lastUpdateDateTime;
    @Transient
    private Boolean isRerunable;

    @Transient
    private WorkitemTemplate workitemTemplate;
    @Transient
    private ApplicationContext applicationContext;
    @Transient
    private SimpleWorkitem nextWorkitem;

    public Long getWorkitemTemplateId() {
        return workitemTemplateId;
    }

    @Autowired
    private SimpleWorkflowExceptionHandler exceptionHandler;

    public SimpleWorkflowExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(SimpleWorkflowExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public Boolean getRerunable() {
        return isRerunable;
    }

    public void setRerunable(Boolean rerunable) {
        isRerunable = rerunable;
    }

    public void setWorkitemTemplateId(Long workitemTemplateId) {
        this.workitemTemplateId = workitemTemplateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public SimpleWorkitem getNextWorkitem() {
        return nextWorkitem;
    }

    public void setNextWorkitem(SimpleWorkitem nextWorkitem) {
        this.nextWorkitem = nextWorkitem;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Long getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(Long workitemId) {
        this.workitemId = workitemId;
    }

    public WorkitemTemplate getWorkitemTemplate() {
        return workitemTemplate;
    }

    public void setWorkitemTemplate(WorkitemTemplate workitemTemplate) {
        this.workitemTemplate = workitemTemplate;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public enum STATUS {
        PENDING, INPROGRESS, COMPLETED, ERROR
    }

    public STATUS process(SimpleWorkflowEvent simpleWorkflowEvent, SimpleWorkflow simpleWorkflow) {
        logger.info("Workitem " + this.getWorkitemId() + " start processing...");
        if (this.status == STATUS.COMPLETED && !isRerunable) {
            return this.status;
        } else {
            String errorMessage = "";
            STATUS statusAfterProcess = handleEvent(simpleWorkflowEvent, errorMessage, simpleWorkflow);
            if (statusAfterProcess == STATUS.ERROR) {
                logger.error("process error: " + errorMessage + " workevent: " + simpleWorkflowEvent + " workitem Id: " + this.getWorkitemId());
                this.exceptionHandler.handleWorkitemErrorException(simpleWorkflow, simpleWorkflowEvent, this, errorMessage);

            } else if (statusAfterProcess == STATUS.COMPLETED) {
                logger.info(" workitem Id " + this.getWorkitemId() + " proceed completed.");
            }
            return statusAfterProcess;
        }
    }
    public abstract STATUS handleEvent(SimpleWorkflowEvent simpleWorkflowEvent, String errorMessage, SimpleWorkflow simpleWorkflow);
    public abstract STATUS haneleRejectEventOnStartedStatus(SimpleWorkflowEvent simpleWorkflowEvent);
}
