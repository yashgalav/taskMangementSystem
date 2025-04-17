package com.taskManagementSystem.repository;

import com.taskManagementSystem.model.BoardXUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardXUserRepository extends JpaRepository<BoardXUser, Long> {
    BoardXUser findFirstByUserId(Long id);

    Optional<BoardXUser> findByUserIdAndBoardId(Long userId, Long boardId);

    void deleteAllByBoardId(Long boardId);
}
