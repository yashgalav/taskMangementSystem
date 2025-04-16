package com.taskManagementSystem.utils;

public class TaskUtil {

    public static String trim(String str){
        if(str.isEmpty() || str.isBlank() || null == str)
            return str;
        return str.trim();
    }
}
