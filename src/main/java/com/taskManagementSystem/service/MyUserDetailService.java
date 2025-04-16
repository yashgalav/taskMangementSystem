package com.taskManagementSystem.service;

import com.taskManagementSystem.model.User;
import com.taskManagementSystem.repository.UserRepository;
import com.taskManagementSystem.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
         User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));

        String name = "";
        if (null != user.getFirstName()) {
            name = user.getFirstName();
            if (null != user.getLastName()) name = name + " " + user.getLastName();
        }

        UserPrincipal userPrincipal = UserPrincipal
                 .builder()
                .username(user.getUsername())
                .name(name)
                .userRole(user.getUserRole())
                .password(user.getPassword())
                .build();

        return  userPrincipal;
    }
}
