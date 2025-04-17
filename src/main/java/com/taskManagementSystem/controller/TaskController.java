package com.taskManagementSystem.controller;

import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.Task;
import com.taskManagementSystem.service.TaskService;
import com.taskManagementSystem.vo.UpdateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> taskCreate(@Validated @RequestBody final Task task) {
        Map<String, Object> response = new HashMap<>();

        try {
            taskService.createTask(task);
        } catch (DataAccessException e) {
            throw new CustomException("Error in inserting data",
                    HttpStatus.BAD_REQUEST);
        }
        response.put("message", "Task created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateTaskPriority(@Validated @RequestBody final UpdateTask task) {
        Map<String, Object> response = new HashMap<>();

        try {
            taskService.updateTask(task);
        } catch (DataAccessException e) {
            throw new CustomException("Error in inserting data",
                    HttpStatus.BAD_REQUEST);
        }

        response.put("message", "Task Updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUsersAllTask(@Validated @PathVariable(name = "userId") Long userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Task> tasks = taskService.getUsersAllTask(userId);
            response.put("tasks", tasks);
        } catch (DataAccessException e) {
            throw new CustomException("Error in Accessing the data",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}/{taskId}")
    public ResponseEntity<Map<String, Object>> deleteTask(@Validated @PathVariable(name = "userId") Long userId,
                                                          @Validated @PathVariable(name = "taskId") Long taskId) {
        Map<String, Object> response = new HashMap<>();

        try {
            taskService.deleteTask(userId, taskId);

        } catch (DataAccessException e) {
            throw new CustomException("Error in Accessing the data",
                    HttpStatus.BAD_REQUEST);
        }
        response.put("Message", "Task deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> getAllTask(@Validated @PathVariable(name = "boardId") Long boardId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Task> tasks = taskService.getAllTask(boardId);
            response.put("tasks", tasks);
        } catch (DataAccessException e) {
            throw new CustomException("Error in Accessing the data",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
