package com.justynsoft.simpleworkflow.personaldayoff;

import java.util.Date;
import java.util.List;

public class DayOffMessage {
    private long employeeID;
    private String employeeName;
    private String requestMessage;
    private List<String> flowMsg;
    private Date offDate;

    public long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(long employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public List<String> getFlowMsg() {
        return flowMsg;
    }

    public void setFlowMsg(List<String> flowMsg) {
        this.flowMsg = flowMsg;
    }

    public Date getOffDate() {
        return offDate;
    }

    public void setOffDate(Date offDate) {
        this.offDate = offDate;
    }
}
