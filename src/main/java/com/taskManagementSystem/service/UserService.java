package com.taskManagementSystem.service;

import com.taskManagementSystem.enums.UserRoleEnum;
import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.User;
import com.taskManagementSystem.repository.UserRepository;
import com.taskManagementSystem.security.JwtHelper;
import com.taskManagementSystem.utils.TaskUtil;
import com.taskManagementSystem.vo.SigninVo;
import com.taskManagementSystem.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired(required = true)
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;



    public User createUser(User user) throws UsernameNotFoundException {

        try{
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

            if(user.getUserRole().equals(UserRoleEnum.EDITOR_ROLE.getUserRole()))
                user.setUserRole(UserRoleEnum.EDITOR_ROLE.getUserRole());
            else if (user.getUserRole().equals(UserRoleEnum.VIEWER_ROLE.getUserRole())) {
                user.setUserRole(UserRoleEnum.VIEWER_ROLE.getUserRole());
            }else{
                throw new CustomException("Role should be either 'Viewer' or 'Editor'.", HttpStatus.BAD_REQUEST);
            }

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserVo signinUser(SigninVo user) {
        try {
            User userDB = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User '" + user.getUsername() + "' not found"));

            String role = null;
            if(userDB.getUserRole().equals(UserRoleEnum.EDITOR_ROLE.getUserRole()))
                role = UserRoleEnum.EDITOR_ROLE.getUserRole();
            else
                role = UserRoleEnum.VIEWER_ROLE.getUserRole();

            UserVo userVo = UserVo
                    .builder()
                    .firstName(userDB.getFirstName())
                    .lastName(userDB.getLastName())
                    .username(userDB.getUsername())
                    .userRole(role)
                    .build();

            return userVo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
