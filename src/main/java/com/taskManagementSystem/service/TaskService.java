package com.taskManagementSystem.service;

import com.taskManagementSystem.enums.UserRoleEnum;
import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.BoardXUser;
import com.taskManagementSystem.model.Task;
import com.taskManagementSystem.model.TaskBoard;
import com.taskManagementSystem.model.User;
import com.taskManagementSystem.repository.BoardXUserRepository;
import com.taskManagementSystem.repository.TaskBoardRepository;
import com.taskManagementSystem.repository.TaskRepository;
import com.taskManagementSystem.repository.UserRepository;
import com.taskManagementSystem.utils.TaskUtil;
import com.taskManagementSystem.vo.TaskVo;
import com.taskManagementSystem.vo.UpdateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskBoardRepository taskBoardRepository;

    @Autowired
    private BoardXUserRepository boardXUserRepository;


    public void createTask(Task task) {

        if(null == task.getUserId())
            throw new CustomException("UserId can't be null", HttpStatus.BAD_REQUEST);

        BoardXUser boardXUser = boardXUserRepository.findByUserIdAndBoardId(task.getUserId(), task.getBoardId())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        if(Boolean.TRUE.equals(boardXUser.getIsBoardOwner())) {

            if (TaskUtil.isEmpty(task.getDescription())
                    || TaskUtil.isEmpty(task.getTitle())
                    || TaskUtil.isEmpty(task.getPriorityTag())
                    || TaskUtil.isEmpty(task.getStatus()))
                throw new CustomException("Content can't be empty", HttpStatus.NO_CONTENT);


            if (null == task.getCreatedAt())
                task.setCreatedAt(LocalDateTime.now());
            if (null == task.getUpdatedAt())
                task.setUpdatedAt(LocalDateTime.now());

            String priority = TaskUtil.getPriority(task.getPriorityTag());
            String status = TaskUtil.getStatus(task.getStatus());

            task.setStatus(status);
            task.setPriorityTag(priority);

            taskRepository.save(task);
        }else{
            throw new CustomException("Only Editor can create task", HttpStatus.BAD_REQUEST);
        }
    }

    public void updateTask(UpdateTask task) {

        if(null == task.getTaskId())
            throw  new CustomException("Task can't be null", HttpStatus.BAD_REQUEST);

        if(null == task.getUserId())
            throw new CustomException("UserId can't be null", HttpStatus.BAD_REQUEST);

        Task taskDb = taskRepository.findByIdAndBoardId(task.getTaskId(),task.getBoardId())
                        .orElseThrow(() -> new CustomException("Task not found", HttpStatus.NOT_FOUND));

        BoardXUser boardXUser = boardXUserRepository.findByUserIdAndBoardId(task.getUserId(), task.getBoardId())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        if(Boolean.TRUE.equals(boardXUser.getIsBoardOwner())){

            taskDb.setUpdatedAt(LocalDateTime.now());

            if(!TaskUtil.isEmpty(task.getPriorityTag())){
                String priority = TaskUtil.getPriority(task.getPriorityTag());
                taskDb.setPriorityTag(priority);
            }

            if(!TaskUtil.isEmpty(task.getTitle()))
                taskDb.setTitle(task.getTitle());

            if(!TaskUtil.isEmpty(task.getDescription()))
                taskDb.setDescription(task.getDescription());


            if(!TaskUtil.isEmpty(task.getStatus())){
                String status = TaskUtil.getStatus(task.getStatus());
                taskDb.setStatus(status);
            }
            taskRepository.save(taskDb);
        }else{
            throw new CustomException("Only original Editor can make changes", HttpStatus.NOT_MODIFIED);
        }

    }

    public List<TaskVo> getUsersAllTask(Long userId) {

        if(null == userId)
            throw new CustomException("UserId can't be null", HttpStatus.BAD_REQUEST);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        BoardXUser boardXUser = boardXUserRepository.findFirstByUserId(userId);

        List<Task> tasks = taskRepository.findAllByUserIdAndBoardId(userId, boardXUser.getBoardId());
        Set<Long> userIds = tasks.stream().map(Task::getUserId).collect(Collectors.toSet());
        Map<Long, String> userNameById = TaskUtil.getStaffMap(userRepository, userIds);

        List<TaskVo> taskVos = new ArrayList<>();
        for(Task task : tasks){
            TaskVo taskVo = TaskVo
                    .builder()
                    .name(userNameById.get(task.getUserId()))
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .priorityTag(task.getPriorityTag())
                    .status(task.getStatus())
                    .createdAt(task.getCreatedAt())
                    .boardId(task.getBoardId())
                    .id(task.getId())
                    .userId(task.getUserId())
                    .build();
            taskVos.add(taskVo);
        }
        return taskVos;
    }

    public void deleteTask(Long userId, Long taskId) {

        if(null == taskId)
            throw  new CustomException("Task can't be null", HttpStatus.BAD_REQUEST);

        if(null == userId)
            throw new CustomException("UserId can't be null", HttpStatus.BAD_REQUEST);

        Task taskDb = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException("Task not found", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));


        if( user.getId().equals(taskDb.getUserId())
                && user.getUserRole().equals(UserRoleEnum.EDITOR_ROLE.getUserRole())){

            taskRepository.delete(taskDb);
        }else{
            throw new CustomException("Only original Editor can make changes", HttpStatus.NOT_MODIFIED);
        }

    }

    public List<TaskVo> getAllTask(Long boardId) {
        Optional<TaskBoard> taskBoard = taskBoardRepository.findById(boardId);

        if(taskBoard.isEmpty())
            throw new CustomException("Task Board not found",HttpStatus.NOT_FOUND);

        List<Task> tasks = taskRepository.findAllByBoardId(boardId);
        Set<Long> userIds = tasks.stream().map(Task::getUserId).collect(Collectors.toSet());
        Map<Long, String> userNameById = TaskUtil.getStaffMap(userRepository, userIds);

        List<TaskVo> taskVos = new ArrayList<>();
        for(Task task : tasks){
            TaskVo taskVo = TaskVo
                    .builder()
                    .name(userNameById.get(task.getUserId()))
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .priorityTag(task.getPriorityTag())
                    .status(task.getStatus())
                    .createdAt(task.getCreatedAt())
                    .boardId(task.getBoardId())
                    .id(task.getId())
                    .userId(task.getUserId())
                    .build();
            taskVos.add(taskVo);
        }
        return taskVos;
    }
}
