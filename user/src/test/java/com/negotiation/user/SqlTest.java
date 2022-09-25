package com.negotiation.user;

import com.negotiation.user.mapper.UserMapper;
import com.negotiation.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class SqlTest {

    @Autowired
    private IUserService userService;

    @Resource
    private UserMapper userMapper;

    @Test
    public void mySqlTest() {
        System.out.println("userMapper.mySqlTest() = " + userMapper.mySqlTest());
    }
}
