package com.huayu.entity;

public class ProjectStart {
    private Integer id;

    private String projectType;

    private String projectName;

    private String address;

    private String projectNumber;

    private String contractNumber;

    private String floor;

    private String projectPrice;

    private String startDatePlan;

    private String endDatePlan;

    private String actualDate;

    private String planDays;

    private String condition;

    private String content;

    private String reason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType == null ? null : projectType.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber == null ? null : projectNumber.trim();
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber == null ? null : contractNumber.trim();
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    public String getProjectPrice() {
        return projectPrice;
    }

    public void setProjectPrice(String projectPrice) {
        this.projectPrice = projectPrice == null ? null : projectPrice.trim();
    }

    public String getStartDatePlan() {
        return startDatePlan;
    }

    public void setStartDatePlan(String startDatePlan) {
        this.startDatePlan = startDatePlan == null ? null : startDatePlan.trim();
    }

    public String getEndDatePlan() {
        return endDatePlan;
    }

    public void setEndDatePlan(String endDatePlan) {
        this.endDatePlan = endDatePlan == null ? null : endDatePlan.trim();
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate == null ? null : actualDate.trim();
    }

    public String getPlanDays() {
        return planDays;
    }

    public void setPlanDays(String planDays) {
        this.planDays = planDays == null ? null : planDays.trim();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }
}