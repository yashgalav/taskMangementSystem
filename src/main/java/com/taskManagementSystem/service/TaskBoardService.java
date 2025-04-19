package com.taskManagementSystem.service;

import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.TaskBoard;
import com.taskManagementSystem.repository.BoardXUserRepository;
import com.taskManagementSystem.repository.CommentRepository;
import com.taskManagementSystem.repository.TaskBoardRepository;
import com.taskManagementSystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskBoardService {

    @Autowired
    private TaskBoardRepository taskBoardRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardXUserRepository boardXUserRepository;



    public TaskBoard createTaskBoard(TaskBoard taskBoard) {

        if(null == taskBoard.getBoardOwnerId() || null == taskBoard.getBoardName())
            throw new CustomException("Id or taskname is null", HttpStatus.BAD_REQUEST);

        taskBoard.setCreatedAt(LocalDateTime.now());
        taskBoard.setUpdatedAt(LocalDateTime.now());

        return taskBoardRepository.save(taskBoard);
    }


    @Transactional
    public void deleteTaskBoard(Long boardId) {

       if(boardId == null){
           throw new CustomException("Board Id can't be null",HttpStatus.NOT_ACCEPTABLE);
       }

       taskRepository.deleteAllByBoardId(boardId);
       commentRepository.deleteAllByBoardId(boardId);
       boardXUserRepository.deleteAllByBoardId(boardId);
       taskBoardRepository.deleteById(boardId);
    }
}
