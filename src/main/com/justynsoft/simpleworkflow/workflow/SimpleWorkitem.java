package com.justynsoft.simpleworkflow.workflow;

import com.justynsoft.simpleworkflow.template.WorkitemTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Date;

public abstract class SimpleWorkitem<T extends SimpleWorkflowEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SimpleWorkitem.class);
    private Long workitemId;
    private Long workflowId;
    private Long workitemTemplateId;
    private STATUS status;
    private Date createDate;
    private Date lastUpdateDateTime;
    private Boolean isRerunable = false;

    private WorkitemTemplate workitemTemplate;
    private ApplicationContext applicationContext;
    private SimpleWorkitem nextWorkitem;
    @Autowired
    private SimpleWorkflowExceptionHandler exceptionHandler;


    public Long getWorkitemTemplateId() {
        return workitemTemplateId;
    }

    public SimpleWorkflowExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(SimpleWorkflowExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public SimpleWorkitemEntity toEntity(){
        return new SimpleWorkitemEntity(this);
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
        PENDING, INPROGRESS, COMPLETED, ERROR, REJECTED
    }

    public STATUS reject(T workflowEvent) {
        logger.info(" Workitem received reject request. Workitem Id: " + this.getWorkitemId());
        if (status == STATUS.COMPLETED || status == STATUS.REJECTED) {
            logger.info(" Nothing to proceed, the workitem is already in status : " + status);
            return status;
        } else if (status == STATUS.ERROR || status == STATUS.PENDING) {
            logger.info(" this workitem will flip to rejected status because current status is : " + status);
            status = STATUS.REJECTED;
            destroyWorkitem();
            return status;
        } else {
            status = handleRejectEventOnStartedStatus(workflowEvent);
            if (status == STATUS.REJECTED) {
                logger.info(" workitem id : " + this.getWorkitemId() + " got rejected.");
            } else {
                logger.info("Workitem can not get rejected.");
            }
            return status;
        }
    }

    public STATUS process(T simpleWorkflowEvent, SimpleWorkflow simpleWorkflow) {
        if (this.status == STATUS.COMPLETED && !isRerunable) {
            return this.status;
        } else {
            logger.info("Workitem " + this.getWorkitemId() + " start processing...");
            String errorMessage = "";
            STATUS statusAfterProcess = handleEvent(simpleWorkflowEvent, errorMessage, simpleWorkflow);
            this.status = statusAfterProcess;
            if (statusAfterProcess == STATUS.ERROR) {
                logger.error("process error: " + errorMessage + " workevent: " + simpleWorkflowEvent + " workitem Id: " + this.getWorkitemId());
                this.exceptionHandler.handleWorkitemErrorException(simpleWorkflow, simpleWorkflowEvent, this, errorMessage);

            } else if (statusAfterProcess == STATUS.COMPLETED) {
                logger.info(" workitem Id " + this.getWorkitemId() + " proceed completed.");
            }
            return statusAfterProcess;
        }
    }

    public abstract STATUS handleEvent(T simpleWorkflowEvent, String errorMessage, SimpleWorkflow simpleWorkflow);

    public abstract STATUS handleRejectEventOnStartedStatus(T simpleWorkflowEvent);

    public abstract void destroyWorkitem();
}
