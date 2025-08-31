package com.jshang.chat.controller;

import com.jshang.chat.pojo.RestResponse;
import com.jshang.chat.pojo.dto.user.UserValidateDTO;
import com.jshang.chat.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 用户鉴权相关web接口
 * @Date 2025/8/31 12:00
 * @Author Shawn
 */
@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserAuthService userAuthService;

    /**
     * 生成一个Token
     */
    @GetMapping("/token")
    public RestResponse<String> getToken(UserValidateDTO request) {
        return RestResponse.ok(userAuthService.getToken(request));
    }

}
