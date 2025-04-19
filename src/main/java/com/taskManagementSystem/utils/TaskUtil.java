package com.taskManagementSystem.utils;

import com.taskManagementSystem.enums.PriorityTagEnum;
import com.taskManagementSystem.enums.StatusEnum;
import com.taskManagementSystem.enums.UserRoleEnum;
import com.taskManagementSystem.exception.CustomException;
import com.taskManagementSystem.model.User;
import com.taskManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class TaskUtil {

    public static String trim(String str){
        if(str.isEmpty() || str.isBlank() || null == str)
            return str;
        return str.trim();
    }

    public static Boolean isEmpty(String str){
        if(str.isEmpty() || str.isBlank() || null == str)
            return true;
        return false;
    }

    public static String getStatus(String status){
        String result = null;
        if(status.equals(StatusEnum.COMPLETED.getStatusEnum())){
            result = StatusEnum.COMPLETED.getStatusEnum();
        }
        else if(status.equals(StatusEnum.INPROGRESS.getStatusEnum())){
            result = StatusEnum.INPROGRESS.getStatusEnum();
        }
        else if(status.equals(StatusEnum.TODO.getStatusEnum())){
            result = StatusEnum.TODO.getStatusEnum();
        }else{
            throw new CustomException("Status can be Completed/In Progress/Todo.", HttpStatus.BAD_REQUEST);
        }
        return  result;
    }


    public static String getPriority(String priority){
        String result = null;

        if(priority.equals(PriorityTagEnum.HIGH.getPriorityTag())) {
            result = PriorityTagEnum.HIGH.getPriorityTag();
        }
        else if (priority.equals(PriorityTagEnum.MEDIUM.getPriorityTag())) {
            result = PriorityTagEnum.MEDIUM.getPriorityTag();
        }
        else if (priority.equals(PriorityTagEnum.LOW.getPriorityTag())) {
            result = PriorityTagEnum.LOW.getPriorityTag();
        }else {
            throw new CustomException("Priority can be High/Medium/Low.",HttpStatus.BAD_REQUEST);
        }
        return result;
    }


    public static String getRole(String userRole){
        String role = null;
        if(userRole.equals(UserRoleEnum.EDITOR_ROLE.getUserRole()))
            role = UserRoleEnum.EDITOR_ROLE.getUserRole();
        else if (userRole.equals(UserRoleEnum.VIEWER_ROLE.getUserRole()))
            role = UserRoleEnum.VIEWER_ROLE.getUserRole();
        else
            throw new CustomException("Role should be either 'Viewer' or 'Editor'.", HttpStatus.BAD_REQUEST);

        return role;
    }

    public static String getStaffName(String firstName , String lastName){
        String fullName = "";

        if(!isEmpty(firstName))
            fullName += firstName.toUpperCase();

        if (!isEmpty(lastName))
            fullName += " "+lastName.toUpperCase();

        if(isEmpty(fullName))
            fullName += "UnKnown";

        return fullName;
    }

    public static Map<Long, String> getStaffMap(UserRepository userRepository, Set<Long> userIds){
        if(userIds.isEmpty())
            throw new CustomException("User Not found", HttpStatus.NOT_FOUND);
        List<User> users = userRepository.findByIdIn(userIds);
        Map<Long, String> staffNameById = users.stream()
                .collect(Collectors
                        .toMap(User::getId,
                                user -> TaskUtil.getStaffName(
                                        user.getFirstName(),
                                        user.getLastName())));
        return staffNameById;
    }
}
