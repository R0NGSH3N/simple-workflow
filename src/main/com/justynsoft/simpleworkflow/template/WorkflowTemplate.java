package com.justynsoft.simpleworkflow.template;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "workflow_setup")
public class WorkflowTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long templateId;
    @NotNull
    private String name;
    @NotNull
    @Column(name = "create_datetime")
    private Date createDate;
    @NotNull
    private String description;
    @Transient
    private List<WorkitemTemplate> workitemTemplates;
    @Transient
    private Long startWorkitemTemplateId;

    //default constructor for JPA
    protected WorkflowTemplate() {
    }

    public Long getStartWorkitemTemplateId() {
        return startWorkitemTemplateId;
    }

    public void setStartWorkitemTemplateId(Long startWorkitemTemplateId) {
        this.startWorkitemTemplateId = startWorkitemTemplateId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WorkitemTemplate> getWorkitemTemplates() {
        return workitemTemplates;
    }

    public void setWorkitemTemplates(List<WorkitemTemplate> workitemTemplates) {
        this.workitemTemplates = workitemTemplates;
    }
}
