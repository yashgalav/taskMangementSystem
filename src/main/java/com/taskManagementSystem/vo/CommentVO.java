package com.taskManagementSystem.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CommentVO {

    private Long id;

    private String name;

    private Long userId;

    private String comment;

    private Long boardId;

    private Long taskId;
}
