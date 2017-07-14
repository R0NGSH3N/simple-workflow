package com.justynsoft.simpleworkflow.personaldayoff;

import com.justynsoft.simpleworkflow.workflow.SimpleWorkflow;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkitem;

public class HumanResourceWorkdaysBackupWI extends SimpleWorkitem<DayOffWorkflowEvent> {
    @Override
    public STATUS handleEvent(DayOffWorkflowEvent simpleWorkflowEvent, String errorMessage, SimpleWorkflow simpleWorkflow) {
        if(getStatus() == STATUS.PENDING){
            DayOffMessage message = simpleWorkflowEvent.getAttachecObject();
            message.getFlowMsg().add(" workitem Id: " + getWorkitemId() + " HR database backed up.");
            return STATUS.COMPLETED;
        }
        return getStatus();
    }

    @Override
    public STATUS handleRejectEventOnStartedStatus(DayOffWorkflowEvent simpleWorkflowEvent) {
        return null;
    }

    @Override
    public void destroyWorkitem() {

    }
}
