# Why we need another workflow?
Because I found current all workflow engine too complicated to use.

# Goal
Create a simple, easy-to-use workflow, extendable workflow.

# Features of simple-workflow
*  Based on Spring Boots
*  Workflow driven by database
*  Async Event driven

# Structure
## Workitem
Workitem is an action item. This is where user need to write the function. Example:

    public class SendDayOffRequestWI
    extends SimpleWorkitem<DayOffWorkflowEvent>{

    @Override
    public STATUS handleEvent(DayOffWorkflowEvent simpleWorkflowEvent, String errorMessage) {
      DayOffMessage message = simpleWorkflowEvent.getAttachecObject();

      if(getStatus() == STATUS.PENDING){
        validateData(message);
        saveData(message);
        return STATUS.COMPLETED;
      }
        return getStatus();
    }


    @Override
    public STATUS handleRejectEventOnStartedStatus(DayOffWorkflowEvent simpleWorkflowEvent) {
      return null;
    }

    @Override
    public void destroyWorkitem() {}
    }
This is the simple workitem, each workitem have to extend __SimpleWorkitem__ with genetic event(we will discuss this in event section).

User need to implements __handleEvent__, __handleRejectEventOnStartedStatus__ and __destroy__ methods.

* handleEvent() method
This is the main method that take care of the business. It has 3 parameters:

  1. WorkflowEvent: The is event object contain information in/out of workitem.

  2. errorMessage: error message for workitem to carry the message in/out of workitem.

* handleRejectEventOnStartedStatus() method
When workflow receive __reject__ request, workflow will do a roll call on each workitem, when workitem is *pending*, workflow will flip it to completed right away, if workitem is *completed* state, then nothing will happen, when workitem in *error* state, then workflow will flip it to completed state.

  Now, the issue is when workitem is in *inprogress* state. Workflow doesn't know how to set the state of workitem, this will leave to workitem. That is why __handleRejcteEventOnStartedStatus__ method. __Workitem can decide which state that need to set.__

* destroy() method
This is destructor of workitem. When Workflow received the reject request, workflow will call destory() method to clean up.
