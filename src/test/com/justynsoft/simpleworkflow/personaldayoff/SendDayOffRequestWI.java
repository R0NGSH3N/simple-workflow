package com.justynsoft.simpleworkflow.personaldayoff;

import com.justynsoft.simpleworkflow.workflow.SimpleWorkflow;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkitem;

public class SendDayOffRequestWI extends SimpleWorkitem<DayOffWorkflowEvent>{
    @Override
    public STATUS handleEvent(DayOffWorkflowEvent simpleWorkflowEvent, String errorMessage, SimpleWorkflow simpleWorkflow) {
        DayOffMessage message = simpleWorkflowEvent.getAttachecObject();
        /**
            This is first workitem, in this case, we don't care of the workitem, it save the data and return completed right away.
        * */
        if(getStatus() == STATUS.PENDING){
            validateData(message);
            saveData(message);
            return STATUS.COMPLETED;
        }
        return getStatus();
    }

    private void validateData(DayOffMessage message){
       message.getFlowMsg().add("workitem Id " + this.getWorkitemId() + " Data Validated");
    }

    private void saveData(DayOffMessage message){
       message.getFlowMsg().add("workitem Id " + this.getWorkitemId() + " Data saved");
    }

    @Override
    public STATUS handleRejectEventOnStartedStatus(DayOffWorkflowEvent simpleWorkflowEvent) {
        return null;
    }

    @Override
    public void destroyWorkitem() {

    }
}
