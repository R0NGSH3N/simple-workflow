package com.justynsoft.simpleworkflow.template;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "workitem_attribute_setup")
public class WorkitemAttributesTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Column(name = "workitem_template_id")
    private long workitemTemplateId;
    @NotNull
    @Column(name = "attribute_name")
    private String attributeName;
    @NotNull
    @Column(name = "attribute_value")
    private String getAttributeValue;

    //default constructor for JPA
    protected WorkitemAttributesTemplate() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWorkitemTemplateId() {
        return workitemTemplateId;
    }

    public void setWorkitemTemplateId(long workitemTemplateId) {
        this.workitemTemplateId = workitemTemplateId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getGetAttributeValue() {
        return getAttributeValue;
    }

    public void setGetAttributeValue(String getAttributeValue) {
        this.getAttributeValue = getAttributeValue;
    }
}
