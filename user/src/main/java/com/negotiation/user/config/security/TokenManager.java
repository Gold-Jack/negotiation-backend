package com.negotiation.user.config.security;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {

    //编码秘钥
    private String tokenSignKey = "20220814";   // 这里暂且用项目开始的日期作为签名

    //1 使用jwt根据用户名生成token
    public String genToken(String username) {
        String token = JWT.create().withAudience(username)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(tokenSignKey));
        return token;
    }
    //2 根据token字符串得到用户信息
    public String getUserInfoFromToken(String token) {
        return String.valueOf(JWT.decode(token).getAudience());     // 这里返回的是用户名
    }
    //3 删除token
    public void removeToken(String token) { }

//    @Test
//    public void manualGenToken() {
//        System.out.println("genToken(\"admin\") = " + genToken("admin"));
//    }
}
