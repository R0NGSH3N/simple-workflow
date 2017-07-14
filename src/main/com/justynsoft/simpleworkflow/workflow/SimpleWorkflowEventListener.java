package com.justynsoft.simpleworkflow.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

public class SimpleWorkflowEventListener implements ApplicationListener<SimpleWorkflowEvent> {
    @Autowired
    private SimpleWorkflowManager workflowManager;

    @Override
    public void onApplicationEvent(SimpleWorkflowEvent workflowEvent) {
        SimpleWorkflow workflow = null;
        //if it is new workflow
        if (workflowEvent.getWorkflowTemplateId() != null && workflowEvent.getWorkflowId() == null) {
            Long newWorkflowId = workflowManager.createWorkflow(workflowEvent.getWorkflowTemplateId());
            workflowEvent.setWorkflowId(newWorkflowId);
        }

        workflow = workflowManager.getWorkflow(workflowEvent.getWorkflowId());
        SimpleWorkflowEventType eventType = workflowEvent.getType();
        if (eventType.equals(SimpleWorkflowEvent.InternalEvent.REJECT_WORKFLOW)) {
            if (workflow != null) {
                workflowManager.rejectWorkflow(workflow, workflowEvent);
            } else {
                throw new RuntimeException("workflow is empty, workflowEvent => " + workflowEvent);
            }
        } else {
            if (workflow != null) {
                workflowManager.runWorkflow(workflow, workflowEvent);
            } else {
                throw new RuntimeException("workflow is empty, workflowEvent => " + workflowEvent);
            }
        }
    }
}
