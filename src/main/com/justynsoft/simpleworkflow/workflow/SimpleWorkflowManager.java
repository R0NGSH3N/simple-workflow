package com.justynsoft.simpleworkflow.workflow;

import com.google.common.collect.Lists;
import com.justynsoft.simpleworkflow.models.*;
import com.justynsoft.simpleworkflow.utils.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleWorkflowManager implements ApplicationEventPublisherAware {
    private static final Logger logger = LoggerFactory.getLogger(SimpleWorkflowManager.class);
    private ApplicationEventPublisher publisher;
    private WorkitemTemplateDAO workitemTemplateDAO;
    private WorkitemAttributesTemplateDAO workitemAttributesTemplateDAO;
    private WorkflowTemplateDAO workflowTemplateDAO;
    private Map<Long, WorkflowTemplate> workflowTemplateMap;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

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
        workflowInstance.setStatus(SimpleWorkflow.STATUS.PEDNGIN);

        List<WorkitemTemplate> workitemTemplateList = workflowTemplate.getWorkitemTemplates();
        workitemTemplateList.forEach(workitemTemplate -> {
            Class classDef = Class.forName(workitemTemplate.getClassName());
            SimpleWorkitem simpleWorkitem = (SimpleWorkitem) classDef.newInstance();
            simpleWorkitem.setWorkitemTemplate(workitemTemplate);
            simpleWorkitem.setApplicationContext(ApplicationContextUtils.getApplicationContext());


        });


    }

    public SimpleWorkflow getWorkflow(Long workflowId, Boolean isNew) {
        if (isNew) {

        }

    }

    public void runWorkflow(SimpleWorkflow workflow, SimpleWorkflowEvent workflowEvent) {

    }
}
