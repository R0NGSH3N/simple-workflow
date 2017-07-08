package com.justynsoft.simpleworkflow.workflow;

public interface SimpleWorkflowExceptionHandler {
    public void handleWorkitemErrorException(SimpleWorkflow simpleWorkflow, SimpleWorkflowEvent simpleWorkflowEvent, SimpleWorkitem simpleWorkitem, String errorMessage);

}
