package com.taskManagementSystem.repository;

import com.taskManagementSystem.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteAllByBoardId(Long boardId);

    List<Comment> findAllByTaskId(Long taskId);
}
