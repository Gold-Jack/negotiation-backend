package com.negotiation.user.config.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.negotiation.user.pojo.User;
import com.negotiation.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.management.ServiceNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt拦截器，用token验证方式，拦截非正常登陆的token持有者
 * @author GoldJack
 * @since 2022/7/14
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    private long hopCount = 0;
    private long lastAccessTime = -1;
    private final long TIME_UNIT = 3000;    // 每次计时时长，单位（毫秒）
    private final long MAX_NONE_TOKEN_ACCESS_COUNT = 5;     // 每秒最多5次无token访问，否则报错

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 从请求头获取token
        String token = request.getHeader("token");

        // 如果handler不一致，直接返回true
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 是否有token
        if (token == null) {
            long currentAccessTime = System.currentTimeMillis();
            if ( !(lastAccessTime == -1 || (currentAccessTime - lastAccessTime) > TIME_UNIT) ) {
                if (hopCount > MAX_NONE_TOKEN_ACCESS_COUNT) {
                    hopCount = 0;
                    throw new RuntimeException("当前无token访问次数过多");
                }
            } else {
                lastAccessTime = currentAccessTime;
                hopCount = 0;
            }
            return false;
        }

        // 是否能从token中正确解析出userId
        Integer userId;
        try {
            userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
//            System.out.println(employeeId);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("token解析错误，请重新登陆");
        }

        // 判断从数据库中是否能根据userId取出对应的User信息
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceNotFoundException("用户不存在，请重新登陆");
        }

        // 密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(
                Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceNotFoundException("token加签验证失败，请重新登陆");
        }
        return true;
    }

}
