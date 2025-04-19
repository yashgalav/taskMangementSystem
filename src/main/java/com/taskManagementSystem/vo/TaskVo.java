package com.taskManagementSystem.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskVo {

    private Long id;

    private String title;

    private String description;

    private String priorityTag;

    private Long userId;

    private Long boardId;

    private String status;

    private LocalDateTime createdAt;

    private String name;
}
