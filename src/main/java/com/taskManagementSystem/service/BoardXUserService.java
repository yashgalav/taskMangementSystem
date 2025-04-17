package com.taskManagementSystem.service;

import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.BoardXUser;
import com.taskManagementSystem.repository.BoardXUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BoardXUserService {

    @Autowired
    private BoardXUserRepository boardXUserRepository;

    public void createBoardXUser(Long boardId, Long userId, Boolean isBoardOwner) {

        if(null == boardId || null == userId || null == isBoardOwner){
            throw new CustomException("Id's can't be null", HttpStatus.BAD_REQUEST);
        }

        BoardXUser boardXUser = new BoardXUser();

        boardXUser.setCreatedAt(LocalDateTime.now());
        boardXUser.setUpdatedAt(LocalDateTime.now());
        boardXUser.setIsBoardOwner(isBoardOwner);
        boardXUser.setBoardId(boardId);
        boardXUser.setUserId(userId);

        boardXUserRepository.save(boardXUser);

    }
}
