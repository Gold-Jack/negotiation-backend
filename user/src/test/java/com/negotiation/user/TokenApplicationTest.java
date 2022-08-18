package com.negotiation.user;

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
        admin.setToken(tokenManager.genToken(admin.getUsername()));
        userService.save(admin);
    }
}
