package com.jshang.chat.common.config.web;

import com.jshang.chat.common.enums.ServiceCodeEnum;
import com.jshang.chat.common.exception.BusinessException;
import com.jshang.chat.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Description JWT校验过滤器，全局生效
 * @Date 2025/8/31 13:29
 * @Author Shawn
 */
@Component
@RequiredArgsConstructor
public class JwtValidateFilter implements WebFilter {
    private final UserAuthService userAuthService;

    /**
     * 不需要JWT验证的URL白名单
     * 注意：这里的路径不包含contextPath，因为RequestPath.value()返回的是相对于contextPath的路径
     * 例如：完整URL为 /chat/user/login，这里只需要配置 /user/login
     */
    private static final List<String> WHITE_LIST = List.of(
            "/chat/user/token"
    );

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getPath().value();
        // 检查是否在白名单中，如果是则直接放行
        if (request.getMethod() == HttpMethod.OPTIONS ||isWhiteListPath(requestPath)) {
            return chain.filter(exchange);
        }

        // 进行JWT验证
        HttpHeaders headers = request.getHeaders();
        List<String> tokens = headers.getOrEmpty("X-JWT-Token");
        if(CollectionUtils.isEmpty(tokens)){
            return Mono.error(new BusinessException(ServiceCodeEnum.BAD_REQUEST.getCode(), "请提供有效的Token"));
        }
        String jwtToken = tokens.get(0);
        if(userAuthService.validToken(jwtToken)){
            return chain.filter(exchange);
        }else {
            return Mono.error(new BusinessException(ServiceCodeEnum.BAD_REQUEST.getCode(), "Token无效"));
        }
    }

    /**
     * 检查请求路径是否在白名单中
     * @param requestPath 请求路径
     * @return 是否在白名单中
     */
    private boolean isWhiteListPath(String requestPath) {
        return WHITE_LIST.stream().anyMatch(requestPath::startsWith);
    }
}
