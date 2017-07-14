package com.justynsoft.simpleworkflow.workflow;

import org.springframework.context.ApplicationEvent;

public class SimpleWorkflowEvent<T> extends ApplicationEvent{
    public enum InternalEvent implements SimpleWorkflowEventType{
        RUN_WORKFLOW("RUN_WORKFLOW"),
        REJECT_WORKFLOW("REJECT_WROKFLOW");

        private String name;
        InternalEvent(String name){
            this.name = name;
        }

        public SimpleWorkflowEventType getEvent(String name){
            return InternalEvent.valueOf(name);
        }

        @Override
        public String getName(){
            return this.name;
        }
    }
    private SimpleWorkflowEventType type;
    private Long workflowTemplateId;
    private Long workflowId;
    private String requestId;
    private T attachecObject;
    private String comment;

    public SimpleWorkflowEvent(Object source){
        super(source);
    }

    public SimpleWorkflowEventType getType() {
        return type;
    }

    public void setType(SimpleWorkflowEventType type) {
        this.type = type;
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

    public T getAttachecObject() {
        return attachecObject;
    }

    public void setAttachecObject(T attachecObject) {
        this.attachecObject = attachecObject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
