package com.justynsoft.simpleworkflow.workflow;

import org.springframework.context.ApplicationListener;

public class SimpleWorkflowEventListener implements ApplicationListener<SimpleWorkflowEvent> {
    private SimpleWorkflowManager workflowManager;

    @Override
    public void onApplicationEvent(SimpleWorkflowEvent workflowEvent) {
        SimpleWorkflow workflow = null;
        //if it is new workflow
        if(workflowEvent.getWorkflowTemplateId() != null && workflowEvent.getWorkflowId() == null){
            Long newWorkflowId = workflowManager.createWorkflow(workflowEvent.getWorkflowTemplateId());
            workflowEvent.setWorkflowId(newWorkflowId);
        }

        workflow = workflowManager.getWorkflow(workflowEvent.getWorkflowId());
        if (workflow != null) {
            workflowManager.runWorkflow(workflow, workflowEvent);
        } else {
            throw new RuntimeException("workflow is empty, workflowEvent => " + workflowEvent);
        }
    }
}
