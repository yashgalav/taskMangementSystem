package com.taskManagementSystem.vo;

import com.taskManagementSystem.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVo {
    private  Long userId;

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String userRole;

    private Long boardId;

}
