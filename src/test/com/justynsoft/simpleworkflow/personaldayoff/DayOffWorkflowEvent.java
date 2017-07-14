package com.justynsoft.simpleworkflow.personaldayoff;

import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowEvent;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowEventType;

public class DayOffWorkflowEvent extends SimpleWorkflowEvent<DayOffMessage> {

    public DayOffWorkflowEvent(Object source){
        super(source);
    }

    public enum DayOffWorkflowEventType implements SimpleWorkflowEventType{
        REQUEST_APPROVED("REQUEST_APPROVED"),
        RUN_WORKFLOW(InternalEvent.RUN_WORKFLOW),
        REJECT_WORKFLOW(InternalEvent.REJECT_WORKFLOW);

        private String name;
        DayOffWorkflowEventType(String name){
            this.name = name;
        }
        DayOffWorkflowEventType(SimpleWorkflowEvent.InternalEvent event){
            this.name = event.getName();
        }

        public SimpleWorkflowEventType getEvent(String name){
            return InternalEvent.valueOf(name);
        }
        @Override
        public String getName(){
            return this.name;
        }
    }
}
