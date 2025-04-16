package com.taskManagementSystem.enums;

public enum PriorityTagEnum {

    HIGH("high"),
    MEDIUM("medium"),
    LOW("low");

    private final String priorityTag;

    PriorityTagEnum(String priorityTag){
        this.priorityTag = priorityTag;
    }

    public String getPriorityTag() {
        return priorityTag;
    }
}
