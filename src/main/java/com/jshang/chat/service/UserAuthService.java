package com.jshang.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jshang.chat.common.enums.ServiceCodeEnum;
import com.jshang.chat.common.exception.BusinessException;
import com.jshang.chat.common.utils.ThreadLocalUtil;
import com.jshang.chat.pojo.dto.user.UserValidateDTO;
import com.jshang.chat.pojo.entity.User;
import com.jshang.chat.pojo.vo.JwtClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @Description 用户鉴权业务类
 * @Date 2025/8/31 13:42
 * @Author Shawn
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthService {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * 生成一个Token
     */
    public String getToken(UserValidateDTO request) {
        // 创建签名密钥
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        // 设置过期时间为24小时
        Date expiration = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        // 获取用户信息并构建JwtClaims
        User userInfo = validateUser(request);
        if(userInfo == null){
            throw new BusinessException(ServiceCodeEnum.UNAUTHORIZED);
        }
        JwtClaims jwtClaims = new JwtClaims(userInfo.getId(), request.getUsername());
        try {
            // 将JwtClaims序列化为JSON字符串
            String userInfoJson = objectMapper.writeValueAsString(jwtClaims);
            // 构建JWT
            return Jwts.builder()
                    .subject(request.getUsername())
                    .issuer(appName)
                    .issuedAt(new Date())
                    .expiration(expiration)
                    .claim("userInfo", userInfoJson)
                    .signWith(key)
                    .compact();
        }catch (Exception e){
            throw new BusinessException(ServiceCodeEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 验证JWT Token是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 检查token是否过期
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                log.warn("JWT Token已过期");
                return false;
            }

            log.debug("JWT Token验证成功，用户: {}", claims.getSubject());

            // 从JWT中获取JSON字符串并反序列化为JwtClaims
            String userInfoJson = claims.get("userInfo", String.class);
            JwtClaims jwtClaims = objectMapper.readValue(userInfoJson, JwtClaims.class);

            ThreadLocalUtil.setJwt(jwtClaims);
            return true;
        } catch (Exception e) {
            log.error("JWT Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    public User validateUser(UserValidateDTO request){
        // TODO 校验逻辑
        return userService.getUserByUsername(request.getUsername());
    }

}
