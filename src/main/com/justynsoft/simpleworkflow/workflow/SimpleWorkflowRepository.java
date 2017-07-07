package com.justynsoft.simpleworkflow.workflow;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleWorkflowRepository {

    private SimpleWorkflowDAO simpleWorkflowDAO;
    private SimpleWorkitemDAO simpleWorkitemDAO;

    public SimpleWorkflow findWorkflowByWorkflowId(Long workflowId){
        SimpleWorkflow workflow = simpleWorkflowDAO.findOne(workflowId);
        List<SimpleWorkitem> workitemList = simpleWorkitemDAO.findByWorkflowId(workflowId);
        workflow.setWorkItemList(workitemList);
        return workflow;
    }

    public SimpleWorkflow insertWorkflow(SimpleWorkflow newWorkflow){
        newWorkflow.getWorkItemList().forEach(simpleWorkitem ->{
            simpleWorkitemDAO.save(simpleWorkitem);
        });
        return simpleWorkflowDAO.save(newWorkflow);
    }
}
