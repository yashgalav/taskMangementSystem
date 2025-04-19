package com.taskManagementSystem.controller;

import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.Comment;
import com.taskManagementSystem.service.CommentService;
import com.taskManagementSystem.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createComment(@Validated @RequestBody Comment comment){
        Comment savedComment = null;
        try{
            savedComment = commentService.createComment(comment);
        }catch (Exception ex){
             throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("comment", savedComment);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@Validated @RequestBody CommentVO commentVO){
        try{
            commentService.deleteComment(commentVO
            );
        }catch (Exception ex){
            throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "comment deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Map<String, Object>> getAllTaskComments(@Validated @PathVariable(name = "taskId") Long taskId){
        List<CommentVO> comments = null;
        try{
             comments = commentService.getAllTaskComments(taskId);
        }catch (Exception ex){
            throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
