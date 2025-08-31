package com.jshang.chat.pojo.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 用户鉴权请求入参
 * @Date 2025/8/31 13:44
 * @Author Shawn
 */
@Data
public class UserValidateDTO implements Serializable {
    /**
     * 用户名
     */
    private String username;
}
