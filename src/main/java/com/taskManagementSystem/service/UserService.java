package com.taskManagementSystem.service;

import com.taskManagementSystem.enums.UserRoleEnum;
import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.BoardXUser;
import com.taskManagementSystem.model.TaskBoard;
import com.taskManagementSystem.model.User;
import com.taskManagementSystem.repository.BoardXUserRepository;
import com.taskManagementSystem.repository.TaskBoardRepository;
import com.taskManagementSystem.repository.UserRepository;
import com.taskManagementSystem.security.JwtHelper;
import com.taskManagementSystem.utils.TaskUtil;
import com.taskManagementSystem.vo.SigninVo;
import com.taskManagementSystem.vo.SignupVo;
import com.taskManagementSystem.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired(required = true)
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private TaskBoardRepository taskBoardRepository;

    @Autowired
    private TaskBoardService taskBoardService;

    @Autowired
    private BoardXUserService boardXUserService;

    @Autowired
    private BoardXUserRepository boardXUserRepository;



    public User createUser(SignupVo signupVo) throws UsernameNotFoundException {

        try{
            User user = signupVo.getUser();

            Optional<User> userDB = userRepository.findByUsername(user.getUsername());

            if (userDB.isPresent())
                throw new CustomException("User already exist !!", HttpStatus.BAD_REQUEST);

            String firstName = TaskUtil.trim(user.getFirstName());
            String lastName = TaskUtil.trim(user.getLastName());
            String password = TaskUtil.trim(user.getPassword());
            String username = TaskUtil.trim(user.getUsername());

            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);

            String role = TaskUtil.getRole(user.getUserRole());
            user.setUserRole(role);

            User savedUser = null;
            if(role.equals(UserRoleEnum.VIEWER_ROLE.getUserRole())) {
                List<TaskBoard> taskBoards = taskBoardRepository.findAll();
                if (taskBoards == null || taskBoards.isEmpty())
                    throw new CustomException("There is no task board found. Please signup as Editor!", HttpStatus.NOT_FOUND);

                savedUser = userRepository.save(user);
                boardXUserService.createBoardXUser(signupVo.getTaskBoard().getId(),
                        savedUser.getId(),
                        Boolean.FALSE);
            }else{
                savedUser = userRepository.save(user);
                signupVo.getTaskBoard().setBoardOwnerId(savedUser.getId());
                TaskBoard taskBoard = taskBoardService.createTaskBoard(signupVo.getTaskBoard());
                boardXUserService.createBoardXUser(taskBoard.getId(),
                        taskBoard.getBoardOwnerId(),
                        Boolean.TRUE);
            }


            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserVo signinUser(SigninVo user) {
        try {
            User userDB = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User '" + user.getUsername() + "' not found"));

            String role = TaskUtil.getRole(userDB.getUserRole());

            BoardXUser boardXUser = boardXUserRepository.findFirstByUserId(userDB.getId());

            UserVo userVo = UserVo
                    .builder()
                    .userId(userDB.getId())
                    .firstName(userDB.getFirstName())
                    .lastName(userDB.getLastName())
                    .username(userDB.getUsername())
                    .boardId(boardXUser.getBoardId())
                    .userRole(role)
                    .build();

            return userVo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
