package com.justynsoft.simpleworkflow.workflow;

import com.google.common.collect.Lists;
import com.justynsoft.simpleworkflow.template.*;
import com.justynsoft.simpleworkflow.utils.ApplicationContextUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleWorkflowManager implements ApplicationEventPublisherAware {
    private static final Logger logger = LoggerFactory.getLogger(SimpleWorkflowManager.class);
    private ApplicationEventPublisher publisher;
    @Autowired
    private WorkitemTemplateDAO workitemTemplateDAO;
    @Autowired
    private WorkitemAttributesTemplateDAO workitemAttributesTemplateDAO;
    @Autowired
    private WorkflowTemplateDAO workflowTemplateDAO;
    @Autowired
    private SimpleWorkflowRepository simpleWorkflowRepository;
    private Map<Long, WorkflowTemplate> workflowTemplateMap;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @PostConstruct
    public void loadTemplates() {
        logger.info("*** Workflow Manager start initialiing...");
        //load all the workitem templates:
        List<WorkitemTemplate> workitemTemplateList = Lists.newArrayList(workitemTemplateDAO.findAll());
        List<WorkitemAttributesTemplate> workitemAttributesTemplateList = Lists.newArrayList(workitemAttributesTemplateDAO.findAll());
        List<WorkflowTemplate> workflowTemplateList = Lists.newArrayList(workflowTemplateDAO.findAll());
        /**
         * first, add all the attributes to workitem.
         */
        for (WorkitemTemplate workitemTemplate : workitemTemplateList) {
            List<WorkitemAttributesTemplate> attributes = new ArrayList<>();
            for (WorkitemAttributesTemplate workitemAttributesTemplate : workitemAttributesTemplateList) {
                if (workitemAttributesTemplate.getWorkitemTemplateId() == workitemTemplate.getTemplateId()) {
                    attributes.add(workitemAttributesTemplate);
                }
            }
            workitemTemplate.setAttributes(attributes);
        }

        /**
         * fullfill the next workitem template
         */
        Map<Long, WorkitemTemplate> workitemTemplateMap = workitemTemplateList.stream().collect(
                Collectors.toMap(WorkitemTemplate::getTemplateId, Function.identity()));
        workitemTemplateList.forEach(workitemTemplate -> {
            workitemTemplate.setNextWorkitemTemplate(workitemTemplateMap.get(workitemTemplate.getNextWorkitemTemplateId()));
        });

        Map<Long, List<WorkitemTemplate>> workitemTemplateMapByWorkflowId = workitemTemplateList.stream().collect(
                Collectors.groupingBy(WorkitemTemplate::getWorkflowTemplateId)
        );

        /**
         * full fill the workflow
         */
        workflowTemplateList.forEach(workflowTemplate -> {
            workflowTemplate.setWorkitemTemplates(workitemTemplateMapByWorkflowId.get(workflowTemplate.getTemplateId()));
        });

        this.workflowTemplateMap = workflowTemplateList.stream().collect(
                Collectors.toMap(WorkflowTemplate::getTemplateId, Function.identity())
        );
    }

    private final SimpleWorkflow instantiateWorkflow(Long workflowTemplateId) {
        WorkflowTemplate workflowTemplate = this.workflowTemplateMap.get(workflowTemplateId);
        if (workflowTemplate == null) {
            throw new RuntimeException(" Can not workflow template for ID: " + workflowTemplateId);
        }

        SimpleWorkflow workflowInstance = new SimpleWorkflow();
        workflowInstance.setTemplate(workflowTemplate);
        workflowInstance.setStatus(SimpleWorkflow.STATUS.PENDING);

        List<WorkitemTemplate> workitemTemplateList = workflowTemplate.getWorkitemTemplates();
        /**
         * assembly workitem list based on the template
         */
        Map<Long, SimpleWorkitem> workitemMap = new HashMap<>();
        workitemTemplateList.forEach(workitemTemplate -> {
            SimpleWorkitem simpleWorkitem = null;
            try {
                Class classDef = Class.forName(workitemTemplate.getClassName());
                simpleWorkitem = (SimpleWorkitem) classDef.newInstance();
                //set up the attributes for workitem
                for (WorkitemAttributesTemplate workitemAttribute : workitemTemplate.getAttributes()) {
                    BeanUtils.setProperty(simpleWorkitem, workitemAttribute.getAttributeName(), workitemAttribute.getGetAttributeValue());
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            simpleWorkitem.setWorkitemTemplate(workitemTemplate);
            simpleWorkitem.setApplicationContext(ApplicationContextUtils.getApplicationContext());
            simpleWorkitem.setStatus(SimpleWorkitem.STATUS.PENDING);
            //we will NOT set workitem ID here, it will be filled later by database
            //simpleWorkitem.setWorkitemId();
            workitemMap.put(workitemTemplate.getTemplateId(), simpleWorkitem);
        });

        //set the next workitem to make workitem chain
        workitemMap.forEach((workitemTemplateId, simpleWorkitem) -> {
                    simpleWorkitem.setNextWorkitem(workitemMap.get(simpleWorkitem.getWorkitemTemplate().getTemplateId()));
                }
        );

        workflowInstance.setWorkItemList(new ArrayList<SimpleWorkitem>(workitemMap.values()));
        return workflowInstance;
    }

    public SimpleWorkflow getWorkflow(Long workflowId) {
        SimpleWorkflow workflow = simpleWorkflowRepository.findWorkflowByWorkflowId(workflowId);
        SimpleWorkflow workflowInstance = instantiateWorkflow(workflow.getWorkflowTemplateId());
        workflowInstance.setWorkflowId(workflow.getWorkflowId());
        workflowInstance.setCreateDate(workflow.getCreateDate());
        workflowInstance.setLastUpdateDateTime(workflow.getLastUpdateDateTime());
        workflowInstance.setStatus(workflow.getStatus());
        Map<Long, SimpleWorkitem> workitemMap = workflow.getWorkItemList().stream().collect(
                Collectors.toMap(SimpleWorkitem::getWorkitemTemplateId, Function.identity()));
        workflowInstance.getWorkItemList().forEach(simpleWorkitem -> {
            SimpleWorkitem workitemDataFromDB = workitemMap.get(simpleWorkitem.getWorkitemTemplate());
            simpleWorkitem.setStatus(workitemDataFromDB.getStatus());
            simpleWorkitem.setWorkitemId(workitemDataFromDB.getWorkitemId());
            simpleWorkitem.setWorkflowId(workitemDataFromDB.getWorkflowId());
            simpleWorkitem.setWorkitemTemplateId(workitemDataFromDB.getWorkitemTemplateId());
        });

        return workflowInstance;
    }

    public Long createWorkflow(Long workflowTemplateId){
        SimpleWorkflow newWorkflow = instantiateWorkflow(workflowTemplateId);
        SimpleWorkflow createdWorkflow = simpleWorkflowRepository.insertWorkflow(newWorkflow);
        return createdWorkflow.getWorkflowId();
    }

    public void runWorkflow(SimpleWorkflow workflow, SimpleWorkflowEvent workflowEvent) {

    }
}
