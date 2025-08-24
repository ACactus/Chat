package com.jshang.chat.controller;

import com.jshang.chat.pojo.RestResponse;
import com.jshang.chat.pojo.entity.User;
import com.jshang.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UserController
 * @Description 用户信息web接口
 * @Date 2025/8/24 9:52
 * @Author Shawn
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get/{id}")
    public RestResponse<User> getUser(@PathVariable Long id){
        return RestResponse.ok(userService.getById(id));
    }
}
