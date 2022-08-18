package com.negotiation.user.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.negotiation.common.util.R;
import com.negotiation.common.util.ResponseCode;
import com.negotiation.user.pojo.User;
import com.negotiation.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.negotiation.common.util.ResponseCode.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 通过ID获取某个用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/get-id/{id}")
    public R getById(@PathVariable Integer id) {
        User oneById = userService.getById(id);
        return R.success(oneById);
    }

    /**
     * 分页读取
     * @param auth 权限
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param search 搜索内容
     * @return 页面信息
     */
    @GetMapping("/load-page")
    public R loadByPage(@RequestParam(defaultValue = "GUEST") String auth,
                        @RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "10") Integer pageSize,
                        @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> userWrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            userWrapper.like(User::getUsername, search);
        }
        // TODO: 权限判断
        Page<User> userPage = userService.page(new Page<User>(pageNum, pageSize), userWrapper);
        return userPage != null ?
                R.success(userPage) : R.error(CODE_311, CODE_311.getCodeMessage());
    }

    /**
     * 用户信息持久化
     * @param user 用户信息
     * @return 是否持久化成功
     */
    @PostMapping("/persistence")
    public R persistence(@RequestBody User user) {
        User deprecatedUser = userService.getOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getUsername, user.getUsername()));

        if (deprecatedUser != null) {   // 有重复的用户名（不允许相同的用户名！）
            return R.error(CODE_301, CODE_301.getCodeMessage());
        }

        user.setAuthority("GUEST");     // 暂时默认新建用户的权限为GUEST

        return userService.save(user) ?
                R.success() : R.error(CODE_313, CODE_313.getCodeMessage());
    }

    /**
     * 更新
     * @param user 新的用户信息
     * @return 是否更新成功
     */
    @PutMapping("/update")
    public R update(@RequestBody User user) {
        return userService.updateById(user) ?
                R.success() : R.error(CODE_310, CODE_310.getCodeMessage());
    }

    /**
     * 删除
     * @param id 用户ID
     * @return ！！普通用户没有删除权限！！
     */
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable Integer id) {
        return R.error(CODE_320, CODE_320.getCodeMessage());
    }
}
