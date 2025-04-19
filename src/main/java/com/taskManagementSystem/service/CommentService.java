package com.taskManagementSystem.service;

import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.BoardXUser;
import com.taskManagementSystem.model.Comment;
import com.taskManagementSystem.model.Task;
import com.taskManagementSystem.model.User;
import com.taskManagementSystem.repository.BoardXUserRepository;
import com.taskManagementSystem.repository.CommentRepository;
import com.taskManagementSystem.repository.TaskRepository;
import com.taskManagementSystem.repository.UserRepository;
import com.taskManagementSystem.utils.TaskUtil;
import com.taskManagementSystem.vo.CommentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private BoardXUserRepository boardXUserRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(CommentService.class);


    public Comment createComment(Comment comment) {

        if(null == comment.getUserId() || null == comment.getBoardId())
            throw new CustomException("Ids can't be null", HttpStatus.BAD_REQUEST);

        BoardXUser boardXUser = boardXUserRepository.findByUserIdAndBoardId(comment.getUserId(), comment.getBoardId())
                .orElseThrow(() -> new CustomException("User doesn't belongs to this Task Board!", HttpStatus.FORBIDDEN));

        Task task = taskRepository.findById(comment.getTaskId())
                .orElseThrow(() -> new CustomException("Task not found", HttpStatus.NOT_FOUND));

        if(null != comment.getId()){
            Optional<Comment> oldComment = commentRepository.findById(comment.getId());
            if(oldComment.isPresent()){
                oldComment.get().setComment(comment.getComment());
                 return commentRepository.save(oldComment.get());
            }
        }


        return commentRepository.save(comment);
    }

    public void deleteComment(CommentVO comment) {

        if(null == comment.getUserId() || null == comment.getBoardId())
            throw new CustomException("Ids can't be null", HttpStatus.BAD_REQUEST);

        BoardXUser boardXUser = boardXUserRepository.findByUserIdAndBoardId(comment.getUserId(), comment.getBoardId())
                .orElseThrow(() -> new CustomException("User doesn't belongs to this Task Board!", HttpStatus.FORBIDDEN));

        Task task = taskRepository.findById(comment.getTaskId())
                .orElseThrow(() -> new CustomException("Task not found", HttpStatus.NOT_FOUND));

        commentRepository.deleteById(comment.getId());

    }

    public List<CommentVO> getAllTaskComments(Long taskId) {

        if(null == taskId)
            throw new CustomException("Ids can't be null", HttpStatus.BAD_REQUEST);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException("Task not found", HttpStatus.NOT_FOUND));

        List<Comment> comments= commentRepository.findAllByTaskId(taskId);
        Set<Long> userId = comments.stream().map(Comment::getUserId).collect(Collectors.toSet());
        Map<Long, String> staffNameById = TaskUtil.getStaffMap(userRepository,userId);

        List<CommentVO> commentVOS = new ArrayList<>();
        for (Comment comment: comments) {
            CommentVO commentVO = CommentVO
                    .builder()
                    .boardId(comment.getBoardId())
                    .userId(comment.getUserId())
                    .comment(comment.getComment())
                    .id(comment.getId())
                    .name(staffNameById.get(comment.getUserId()))
                    .taskId(comment.getTaskId())
                    .build();
            commentVOS.add(commentVO);
        }
        return  commentVOS;
    }

}
