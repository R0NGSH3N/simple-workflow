package com.justynsoft.simpleworkflow.workflow;

import com.justynsoft.simpleworkflow.template.WorkitemTemplate;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "workitem")
public class SimpleWorkitem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="workitem_id")
    private Long workitemId;
    @NotNull
    @Column(name="workflow_id")
    private Long workflowId;
    @NotNull
    @Column(name="workitem_template_id")
    private Long workitemTemplateId;
    @NotNull
    @Column(name="status")
    private STATUS status;
    @Column(name="create_datetime")
    private Date createDate;
    @Column(name="lastupdate_datetime")
    private Date lastUpdateDateTime;

    @Transient
    private WorkitemTemplate workitemTemplate;
    @Transient
    private ApplicationContext applicationContext;
    @Transient
    private SimpleWorkitem nextWorkitem;

    public Long getWorkitemTemplateId() {
        return workitemTemplateId;
    }

    public void setWorkitemTemplateId(Long workitemTemplateId) {
        this.workitemTemplateId = workitemTemplateId;
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

    public SimpleWorkitem getNextWorkitem() {
        return nextWorkitem;
    }
    public void setNextWorkitem(SimpleWorkitem nextWorkitem) {
        this.nextWorkitem = nextWorkitem;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Long getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(Long workitemId) {
        this.workitemId = workitemId;
    }

    public WorkitemTemplate getWorkitemTemplate() {
        return workitemTemplate;
    }

    public void setWorkitemTemplate(WorkitemTemplate workitemTemplate) {
        this.workitemTemplate = workitemTemplate;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public enum STATUS {
        PENDING, INPROGRESS, COMPLETED, ERROR
    }
}
