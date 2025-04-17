package com.taskManagementSystem.enums;

public enum PriorityTagEnum {

    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private final String priorityTag;

    PriorityTagEnum(String priorityTag){
        this.priorityTag = priorityTag;
    }

    public String getPriorityTag() {
        return priorityTag;
    }
}
