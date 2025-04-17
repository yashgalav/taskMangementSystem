package com.taskManagementSystem.vo;

import lombok.Data;

@Data
public class UpdateTask {

    private Long userId;

    private Long taskId;

    private String title;

    private String priorityTag;

    private String Description;

    private String status;

    private Long boardId;
}
