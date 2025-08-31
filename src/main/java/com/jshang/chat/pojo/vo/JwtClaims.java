package com.jshang.chat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description JWTClaims
 * @Date 2025/8/31 12:31
 * @Author Shawn
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtClaims implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;
}
