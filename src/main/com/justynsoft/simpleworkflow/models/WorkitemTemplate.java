package com.justynsoft.simpleworkflow.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "workitem_setup")
public class WorkitemTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long templateId;
    @NotNull
    @Column(name = "workflow_template_id")
    private long workflowTemplateId;
    @NotNull
    private String name;
    @NotNull
    @Column(name = "class_name")
    private String className;
    @NotNull
    @Column(name = "is_rerunnable")
    private Boolean isRerunable;
    @Column(name = "next_workitem_template_id")
    private long nextWorkitemTemplateId;

    @Transient
    private WorkitemTemplate nextWorkitemTemplate;
    @Transient
    private Boolean isStartWorkitem;
    @Transient
    private List<WorkitemAttributesTemplate> attributes;

    //default constructor for JPA
    protected WorkitemTemplate() {
    }

    public long getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(long workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }

    public long getNextWorkitemTemplateId() {
        return nextWorkitemTemplateId;
    }

    public void setNextWorkitemTemplateId(long nextWorkitemTemplateId) {
        this.nextWorkitemTemplateId = nextWorkitemTemplateId;
    }

    public WorkitemTemplate getNextWorkitemTemplate() {
        return nextWorkitemTemplate;
    }

    public void setNextWorkitemTemplate(WorkitemTemplate nextWorkitemTemplate) {
        this.nextWorkitemTemplate = nextWorkitemTemplate;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getRerunable() {
        return isRerunable;
    }

    public void setRerunable(Boolean rerunable) {
        isRerunable = rerunable;
    }

    public Boolean getStartWorkitem() {
        return isStartWorkitem;
    }

    public void setStartWorkitem(Boolean startWorkitem) {
        isStartWorkitem = startWorkitem;
    }

    public List<WorkitemAttributesTemplate> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<WorkitemAttributesTemplate> attributes) {
        this.attributes = attributes;
    }
}
