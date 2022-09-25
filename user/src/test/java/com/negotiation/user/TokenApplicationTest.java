package com.negotiation.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.negotiation.common.config.security.TokenManager;
import com.negotiation.user.pojo.User;
import com.negotiation.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TokenApplicationTest {

    @Autowired(required = false)
    private IUserService userService;
    @Autowired(required = false)
    private TokenManager tokenManager;

    @Test
    public void testTokenLogin() {
        User admin = userService.getById(1);
        admin.setToken(null);
        admin.setToken(tokenManager.genToken(admin.getUsername()));
        userService.updateById(admin);
    }

    @Test
    public void testTokenGenerate() {
        User admin = userService.getById(1);
        admin.setToken(tokenManager.genToken(admin.getUsername()));
        userService.updateById(admin);
    }

    @Test
    public void testTokenLogout() {
        User admin = userService.getById(1);
        System.out.println("before = " + admin);
        admin.setToken(null);
        userService.update(admin,
                Wrappers.<User>lambdaUpdate().set(User::getToken, admin.getToken()));
        admin = userService.getById(1);
        System.out.println("after = " + admin);
    }

}
