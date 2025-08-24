package com.jshang.chat.common.utils;

import com.jshang.chat.pojo.entity.User;

/**
 * @Classname ThreadLocalUtil
 * @Description TODO 逻辑待实现
 * @Date 2025/8/24 13:25
 * @Author Shawn
 */
public class ThreadLocalUtil {

    public static User getUser(){
        User user = new User();
        user.setId(1L);
        user.setUserName("Shawn");
        return user;
    }
}
