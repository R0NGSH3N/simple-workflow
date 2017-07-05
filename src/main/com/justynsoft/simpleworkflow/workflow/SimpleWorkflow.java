package com.justynsoft.simpleworkflow.workflow;

import com.justynsoft.simpleworkflow.models.WorkflowTemplate;

public class SimpleWorkflow {
    private Long workflowId;
    private WorkflowTemplate template;
    private STATUS status;

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public WorkflowTemplate getTemplate() {
        return template;
    }

    public void setTemplate(WorkflowTemplate template) {
        this.template = template;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public enum STATUS {
        PEDNGIN, INPROGRESS, COMPLETED, ERROR
    }
}
