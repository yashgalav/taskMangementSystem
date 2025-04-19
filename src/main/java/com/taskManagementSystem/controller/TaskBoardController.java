package com.taskManagementSystem.controller;

import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.TaskBoard;
import com.taskManagementSystem.service.BoardXUserService;
import com.taskManagementSystem.service.TaskBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class TaskBoardController {

    @Autowired
    private TaskBoardService taskBoardService;

    @Autowired
    private BoardXUserService boardXUserService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> taskBoardCreate(@Validated @RequestBody final TaskBoard taskBoard) {
        Map<String, Object> response = new HashMap<>();

        try {
            TaskBoard taskBoardDB =taskBoardService.createTaskBoard(taskBoard);
            boardXUserService.createBoardXUser(taskBoardDB.getId(),
                    taskBoardDB.getBoardOwnerId(),
                    Boolean.TRUE);

        } catch (DataAccessException e) {
            throw new CustomException("Error in inserting data",
                    HttpStatus.BAD_REQUEST);
        }
        response.put("message", "Task board created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteTaskBoard(@Validated @PathVariable(name = "boardId") Long boardId) {
        Map<String, Object> response = new HashMap<>();

        try {
            taskBoardService.deleteTaskBoard(boardId);

        } catch (Exception e) {
            throw new CustomException(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        response.put("message", "Task board deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
