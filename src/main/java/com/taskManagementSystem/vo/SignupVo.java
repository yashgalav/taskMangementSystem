package com.taskManagementSystem.vo;


import com.taskManagementSystem.model.TaskBoard;
import com.taskManagementSystem.model.User;
import lombok.Data;

@Data
public class SignupVo {

    User user;
    TaskBoard taskBoard;
}
