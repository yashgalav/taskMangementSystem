package com.taskManagementSystem.controller;


import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.User;
import com.taskManagementSystem.security.JwtHelper;
import com.taskManagementSystem.service.UserService;
import com.taskManagementSystem.vo.SigninVo;
import com.taskManagementSystem.vo.SignupVo;
import com.taskManagementSystem.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@Validated @RequestBody final SignupVo signupVo) {
        User newUser = null;
        Map<String, Object> response = new HashMap<>();
        String token = null;

        try {
            newUser = userService.createUser(signupVo);
            token = jwtHelper.generateToken(newUser.getUsername());
            newUser.setPassword(null);
        } catch (DataAccessException e) {
            throw new CustomException("Error in inserting data",
                    HttpStatus.BAD_REQUEST);
        }
        response.put("User", newUser);
        response.put("token", token);
        response.put("message", "User profile created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signin(@Validated @RequestBody final SigninVo user) {
        UserVo newUser = null;
        Map<String, Object> response = new HashMap<>();
        String token = null;

        try {
            newUser = userService.signinUser(user);
            token = jwtHelper.generateToken(newUser.getUsername());
            newUser.setPassword(null);
        } catch (DataAccessException e) {
            throw new CustomException("Error in inserting data",
                    HttpStatus.BAD_REQUEST);
        }
        response.put("User", newUser);
        response.put("token", token);
        response.put("message", "User signin successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
