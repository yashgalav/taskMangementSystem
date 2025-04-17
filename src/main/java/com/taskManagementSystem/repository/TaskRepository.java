package com.taskManagementSystem.repository;

import com.taskManagementSystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository  extends JpaRepository<Task,Long> {

    Optional<Task> findByIdAndBoardId(Long taskId, Long boardId);

    List<Task> findAllByUserIdAndBoardId(Long userId, Long boardId);

    List<Task> findAllByBoardId(Long boardId);

    void deleteAllByBoardId(Long boardId);
}
