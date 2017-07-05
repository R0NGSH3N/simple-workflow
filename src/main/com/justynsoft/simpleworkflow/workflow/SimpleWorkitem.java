package com.justynsoft.simpleworkflow.workflow;

import com.justynsoft.simpleworkflow.models.WorkitemTemplate;
import org.springframework.context.ApplicationContext;

public class SimpleWorkitem {
    private Long workflowId;
    private Long workitemId;
    private WorkitemTemplate workitemTemplate;
    private ApplicationContext applicationContext;

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
}
