package com.justynsoft.simpleworkflow.workflow;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "workitem")
public class SimpleWorkitemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "workitem_id")
    private Long workitemId;
    @Column(name = "workflow_id")
    private Long workflowId;
    @Column(name = "workitem_template_id")
    private Long workitemTemplateId;
    @Column(name = "status")
    private String status;
    @Column(name = "create_datetime")
    private Date createDate;
    @Column(name = "lastupdate_datetime")
    private Date lastUpdateDateTime;

    public SimpleWorkitemEntity(){};

    public SimpleWorkitemEntity(SimpleWorkitem simpleWorkitem){
        this.workflowId = simpleWorkitem.getWorkflowId();
        this.workitemId = simpleWorkitem.getWorkitemId();
        this.workitemTemplateId = simpleWorkitem.getWorkitemTemplateId();
        this.status = simpleWorkitem.getStatus().toString();
        this.createDate = simpleWorkitem.getCreateDate();
        this.lastUpdateDateTime = simpleWorkitem.getLastUpdateDateTime();
    }

    public Long getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(Long workitemId) {
        this.workitemId = workitemId;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Long getWorkitemTemplateId() {
        return workitemTemplateId;
    }

    public void setWorkitemTemplateId(Long workitemTemplateId) {
        this.workitemTemplateId = workitemTemplateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
