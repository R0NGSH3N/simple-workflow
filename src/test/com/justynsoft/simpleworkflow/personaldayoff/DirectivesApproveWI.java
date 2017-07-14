package com.justynsoft.simpleworkflow.personaldayoff;

import com.justynsoft.simpleworkflow.workflow.SimpleWorkflow;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowEventType;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkitem;

public class DirectivesApproveWI extends SimpleWorkitem<DayOffWorkflowEvent> {

    @Override
    public STATUS handleEvent(DayOffWorkflowEvent simpleWorkflowEvent , String errorMessage, SimpleWorkflow simpleWorkflow) {
       /*
       This workitem send day off request to his directives, here, we hard coded the directives, but actually, you can get
       this from database use @autowired.
       * */
       DayOffMessage message = simpleWorkflowEvent.getAttachecObject();
       if(getStatus() == STATUS.PENDING){
           sendMessageToDirective(message);
           return STATUS.INPROGRESS;
       }
       if(getStatus() == STATUS.INPROGRESS ){
           SimpleWorkflowEventType eventType = simpleWorkflowEvent.getType();
           if(eventType.equals(DayOffWorkflowEvent.DayOffWorkflowEventType.REQUEST_APPROVED)){
               message.getFlowMsg().add(" workitem Id: " + this.getWorkitemId() + " Day off Request Approved");
           }
           return STATUS.COMPLETED;
       }
       return this.getStatus();
    }

    private void sendMessageToDirective(DayOffMessage message){
        message.getFlowMsg().add("workitem Id " + this.getWorkitemId() + " Data Validated");
    }

    @Override
    public STATUS handleRejectEventOnStartedStatus(DayOffWorkflowEvent simpleWorkflowEvent) {
        return null;
    }

    @Override
    public void destroyWorkitem() {

    }
}
