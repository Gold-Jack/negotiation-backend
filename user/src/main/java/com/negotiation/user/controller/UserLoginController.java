package com.negotiation.user.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.negotiation.common.config.security.TokenManager;
import com.negotiation.common.util.R;
import com.negotiation.user.pojo.User;
import com.negotiation.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.negotiation.common.util.ResponseCode.*;

@RestController
@RequestMapping("/user")
@ApiModel("用户登陆 前端控制器")
public class UserLoginController {

    @Autowired
    private UserController userController;
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenManager tokenManager;

    /**
     * 用户登陆
     * @param user 用户登陆信息
     * @return 是否登陆成功
     */
    @PostMapping("/login")
    @ApiOperation("用户登陆")
    public R userLogin(@RequestBody User user) {
        // 确保用户名、密码不为空
        assert StrUtil.isNotBlank(user.getUsername());
        assert StrUtil.isNotBlank(user.getPassword());

        User databaseUser = userService.getOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getUsername, user.getUsername()));
        /*
        * 后续如果需求变更，考虑用filter-chain代替if判断
        * */
        // 判断用户名是否存在
        if (databaseUser == null) {
            // 用户名不存在
            return R.error(CODE_302, CODE_302.getCodeMessage());
        }

        // 判断密码是否正确：目前未经过passwordEncode的密码也能正常登陆
        // passwordEncoder.matches(rawPassword, encodedPassword)
        if (!(passwordEncoder.matches(user.getPassword(), databaseUser.getPassword())
            || StrUtil.equals(user.getPassword(), databaseUser.getPassword())) ) {
            // 密码错误
            return R.error(CODE_300, CODE_300.getCodeMessage());
        }

        // 如果没有token，则生成token
        if (databaseUser.getToken() == null) {
            databaseUser.setToken(tokenManager.genToken(databaseUser.getUsername()));
        }

        // 向数据库中更新带token的databaseUser信息
        return userController.update(databaseUser);
    }

    /**
     * 注册
     * @param user 注册用户信息
     * @return 是否注册成功
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")       // TODO 注册完，获取token自动登录
    public R userRegister(@RequestBody User user) {
        // 确保用户名、密码不为空
        assert StrUtil.isNotBlank(user.getUsername());
        assert StrUtil.isNotBlank(user.getPassword());
        // 对注册用户的密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 添加token信息
        user.setToken(tokenManager.genToken(user.getUsername()));
        // 数据库持久化和规则验证的部分，交给UserController完成
        return userController.persistence(user);
    }


    /**
     * 注销
     * @param user 注销的用户信息
     * @return 是否注销成功
     */
    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public R userLogout(@RequestBody User user) {
        User tokenUser = userService.getOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getUsername,  user.getUsername()));
        // 防止未注册用户直接请求注销
        if (tokenUser == null) {
            return R.error(CODE_302, CODE_302.getCodeMessage());
        }
        // 清除用户token信息
        //  - 暂时采用手动清除，后续考虑使用tokenManager.removeToken()的方法
        return userService.update(tokenUser,
                Wrappers.<User>lambdaUpdate().set(User::getToken, tokenUser.getToken())) ?
                R.success() : R.error(CODE_312, CODE_312.getCodeMessage());
    }


}
