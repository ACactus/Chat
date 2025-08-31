package com.jshang.chat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jshang.chat.mapper.UserMapper;
import com.jshang.chat.pojo.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 获取用户信息
     */
    public User getUserByUsername(String username) {
        return lambdaQuery().eq(User::getUserName, username).one();
    }

}
