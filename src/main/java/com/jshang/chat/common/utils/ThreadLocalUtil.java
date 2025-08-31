package com.jshang.chat.common.utils;

import com.jshang.chat.pojo.vo.JwtClaims;

/**
 * @Classname ThreadLocalUtil
 * @Description 用户信息线程存储
 * @Date 2025/8/24 13:25
 * @Author Shawn
 */
public class ThreadLocalUtil {

    private static final ThreadLocal<JwtClaims> JWT_THREAD_LOCAL = new ThreadLocal<>();

    public static Long getUserId(){
        return geJwtClaims().getUserId();
    }

    public static String getUsername(){
        return geJwtClaims().getUsername();
    }

    public static void setJwt(JwtClaims jwtClaims){
        JWT_THREAD_LOCAL.set(jwtClaims);
    }

    public static JwtClaims geJwtClaims(){
        return JWT_THREAD_LOCAL.get();
    }
}
