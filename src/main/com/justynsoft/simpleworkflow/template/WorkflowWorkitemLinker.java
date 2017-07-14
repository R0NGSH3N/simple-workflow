package com.justynsoft.simpleworkflow.template;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "workflow_workitem_links")
public class WorkflowWorkitemLinker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @NotNull
    @Column(name = "workflow_template_id")
    private long workflowTemplateId;
    @NotNull
    @Column(name = "workitem_template_id")
    private long workitemTemplateId;
    @NotNull
    @Column(name = "next_workitem_template_id")
    private long nextWorkitemTemplateId;
    @NotNull
    @Column(name = "is_start_workitem")
    private Boolean isStartWorkitem;

    //default constructor for JPA
    protected WorkflowWorkitemLinker() {
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(long workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }

    public long getWorkitemTemplateId() {
        return workitemTemplateId;
    }

    public void setWorkitemTemplateId(long workitemTemplateId) {
        this.workitemTemplateId = workitemTemplateId;
    }

    public long getNextWorkitemTemplateId() {
        return nextWorkitemTemplateId;
    }

    public void setNextWorkitemTemplateId(long nextWorkitemTemplateId) {
        this.nextWorkitemTemplateId = nextWorkitemTemplateId;
    }

    public Boolean getStartWorkitem() {
        return isStartWorkitem;
    }

    public void setStartWorkitem(Boolean startWorkitem) {
        isStartWorkitem = startWorkitem;
    }
}
