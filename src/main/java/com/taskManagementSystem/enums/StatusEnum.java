package com.taskManagementSystem.enums;

public enum StatusEnum {

    COMPLETED("Completed"),
    TODO("Todo"),
    INPROGRESS("In Progress");

    private final String status;

    StatusEnum(String status){
        this.status = status;
    }

    public String getStatusEnum() {
        return status;
    }
}
