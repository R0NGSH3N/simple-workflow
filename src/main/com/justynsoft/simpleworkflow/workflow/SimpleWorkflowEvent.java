package com.justynsoft.simpleworkflow.workflow;

import org.springframework.context.ApplicationEvent;

public class SimpleWorkflowEvent extends ApplicationEvent {
    private Long workflowId;
    private Long workflowTemplateId;
    private String requestId;
    private Object attachedObject;
    private String comment;
    private Object source;

    public SimpleWorkflowEvent(Long workflowId, String requestId, Object attachedObject, String comment, Object source) {
        super(source);
        this.workflowId = workflowId;
        this.requestId = requestId;
        this.attachedObject = attachedObject;
        this.comment = comment;
    }

    public Long getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(Long workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getAttachedObject() {
        return attachedObject;
    }

    public void setAttachedObject(Object attachedObject) {
        this.attachedObject = attachedObject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
