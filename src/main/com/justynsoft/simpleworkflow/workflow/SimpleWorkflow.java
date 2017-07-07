package com.justynsoft.simpleworkflow.workflow;

import com.justynsoft.simpleworkflow.template.WorkflowTemplate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "workflow")
public class SimpleWorkflow {
    @Id
    @Column(name="workflow_id")
    private Long workflowId;
    @NotNull
    @Column(name="workflow_template_id")
    private Long workflowTemplateId;
    @NotNull
    private STATUS status;
    @Column(name="create_datetime")
    private Date createDate;
    @Column(name="lastupdate_datetime")
    private Date lastUpdateDateTime;

    @Transient
    private List<SimpleWorkitem> workItemList;
    @Transient
    private WorkflowTemplate template;

    public Long getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(Long workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public List<SimpleWorkitem> getWorkItemList() {
        return workItemList;
    }

    public void setWorkItemList(List<SimpleWorkitem> workItemList) {
        this.workItemList = workItemList;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public WorkflowTemplate getTemplate() {
        return template;
    }

    public void setTemplate(WorkflowTemplate template) {
        this.template = template;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public enum STATUS {
        PENDING, INPROGRESS, COMPLETED, ERROR
    }
}
