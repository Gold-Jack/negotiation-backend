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

@RestController
@RequestMapping("/admin/")
public class AdminController {

    @Autowired
    private IUserService userService;

    /**
     * 分页读取
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param search 搜索内容
     * @return 页面信息
     */
    @GetMapping("/get-page")
    public R getByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> userWrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            userWrapper.like(User::getUsername, search);
        }
        Page<User> userPage = userService.page(new Page<User>(pageNum, pageSize), userWrapper);
        return userPage != null ?
                R.success(userPage) : R.error(CODE_311, CODE_311.getCodeMessage());
    }

    /**
     * 通过ID删除
     * @param id
     * @return 被删除的用户ID
     */
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable Integer id) {
        return userService.removeById(id) ?
                R.success(id) : R.error(CODE_312, CODE_312.getCodeMessage());
    }

    /**
     * 通过用户信息删除
     * @param user
     * @return 被删除的用户ID
     */
    @DeleteMapping("/delete")
    public R deleteByEntity(@RequestBody User user) {
        return userService.removeById(user) ?
                R.success(user.getUserId()) : R.error(CODE_312, CODE_312.getCodeMessage());
    }
}
