package com.negotiation.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.negotiation.common.util.R;
import com.negotiation.common.util.ResponseCode;
import com.negotiation.user.pojo.User;
import com.negotiation.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.negotiation.common.util.ResponseCode.*;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private UserController userController;
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 登陆
     * @param user 用户登陆信息
     * @return 是否登陆成功
     */
    @PostMapping("/login")
    public R login(@RequestBody User user) {
        User databaseUser = userService.getOne(
                Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername())
        );

        /*
        * 后续如果需求变更，考虑用filter-chain代替if判断
        * */
        if (databaseUser == null) {    // 用户名不存在
            return R.error(CODE_302, CODE_302.getCodeMessage());
        }

        // passwordEncoder.matches(rawPassword, encodedPassword)
        if (!passwordEncoder.matches(user.getPassword(), databaseUser.getPassword())) {    // 密码错误
            return R.error(CODE_300, CODE_300.getCodeMessage());
        }

        return R.success();
    }

    /**
     * 注册
     * @param user 注册用户信息
     * @return 是否注册成功
     */
    @PostMapping("/register")
    public R register(@RequestBody User user) {
        // 对注册用户的密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 数据库持久化和规则验证的部分，交给UserController完成
        return userController.persistence(user);
    }
}
