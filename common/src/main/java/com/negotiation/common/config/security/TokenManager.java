package com.negotiation.common.config.security;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.negotiation.common.feign.UserFeignService;
import com.negotiation.common.pojo.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * token管理
 * 目前实现：生成token、移除token
 */
@Component
@Getter // 为了JwtInterceptor解析token时，可以用过get方法获取tokenSignKey
public class TokenManager {

    @Resource
    private UserFeignService userService;

    //编码秘钥
    private String tokenSignKey = "20220814";   // 这里暂且用项目开始的日期作为签名

    // 使用jwt根据用户名生成token
    public String genToken(String username) {
        String token = JWT.create().withAudience(username)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))  // token在2小时后过期
                .sign(Algorithm.HMAC256(tokenSignKey));
        return token;
    }
    // 根据token字符串得到用户信息
    public String getUserInfoFromToken(String token) {
        return String.valueOf(JWT.decode(token).getAudience());     // 这里返回的是用户名
    }


    // 删除token
    public boolean removeToken(Object user) {
//        User userToken = (User) user;
//        String token = userToken.getToken();
//        // 目前的逻辑是，直接清除用户的token，并将空token的User对象，还给LoginController
//        return userService.update(user,
//                Wrappers.<User>lambdaUpdate().set(User::getToken, token));
        return true;
    }


}
