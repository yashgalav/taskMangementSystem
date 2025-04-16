package com.taskManagementSystem.model;

import com.taskManagementSystem.enums.PriorityTagEnum;
import com.taskManagementSystem.enums.StatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private PriorityTagEnum priorityTag;

    private Long createdBy;

    private StatusEnum status;

    @CreatedDate
    private String created_at;

    @LastModifiedDate
    private String updated_at;
}
