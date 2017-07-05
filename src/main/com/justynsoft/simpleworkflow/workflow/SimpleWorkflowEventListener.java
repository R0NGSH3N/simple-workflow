package com.justynsoft.simpleworkflow.workflow;

import org.springframework.context.ApplicationListener;

public class SimpleWorkflowEventListener implements ApplicationListener<SimpleWorkflowEvent> {
    private SimpleWorkflowManager workflowManager;

    @Override
    public void onApplicationEvent(SimpleWorkflowEvent workflowEvent) {
        SimpleWorkflow workflow = null;
        if (workflowEvent.getWorkflowId() == null) {
            workflow = workflowManager.getWorkflow(null, Boolean.TRUE);
            workflowEvent.setWorkflowId(workflow.getWorkflowId());
        } else {
            workflow = workflowManager.getWorkflow(workflowEvent.getWorkflowId(), Boolean.FALSE);
        }

        if (workflow != null) {
            workflowManager.runWorkflow(workflow, workflowEvent);
        } else {
            throw new RuntimeException("workflow is empty, workflowEvent => " + workflowEvent);
        }
    }
}
