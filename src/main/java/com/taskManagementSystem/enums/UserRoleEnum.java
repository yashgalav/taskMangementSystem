package com.taskManagementSystem.enums;

public enum UserRoleEnum {

    EDITOR_ROLE("Editor"),
    VIEWER_ROLE("Viewer");

    private final String userRole;

    UserRoleEnum(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }
}
