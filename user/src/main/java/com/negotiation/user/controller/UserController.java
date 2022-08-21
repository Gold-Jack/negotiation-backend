package com.negotiation.user.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.negotiation.common.util.R;
import com.negotiation.user.feign.FileFeignService;
import com.negotiation.user.pojo.User;
import com.negotiation.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @Autowired
    private FileFeignService fileFeignService;

    /**
     * 通过ID获取某个用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/get/{userId}")
    public R getById(@PathVariable Integer userId) {
        User oneById = userService.getById(userId);
        if (oneById == null) {
            return R.error(CODE_302, CODE_302.getCodeMessage());
        }
        return R.success(oneById);
    }

    /**
     * 用过User对象获取用户信息
     * @param user 用户对象
     * @return 用户信息
     */
    @GetMapping("/get")
    public R getByEntity(@RequestBody User user) {
        User entity = userService.getById(user.getUserId());
        if (entity == null) {
            return R.error(CODE_302, CODE_302.getCodeMessage());
        }
        return R.success(entity);
    }

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
     * 用户头像上传
     * @param userId 用户ID
     * @param avatarImage 用户头像文件
     * @return 带有用户头像url的用户对象
     * @throws IOException
     */
    @PostMapping(value = "/upload-avatar-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R uploadUserAvatarImage(@RequestParam Integer userId,
                                   @RequestPart MultipartFile avatarImage) throws IOException {
        R uploadResult;
        try {
            uploadResult = fileFeignService.upload(avatarImage);
        } catch (IOException ioException) {     // 如果文件上传失败，返回错误码
            return R.error(CODE_101, CODE_101.getCodeMessage());
        }
        // 确保upload方法封装的数据是String类型
        assert StrUtil.equals(uploadResult.getData().getClass().getName(), "String");
        // 获取avatarUrl
        String avatarImageUrl = (String) uploadResult.getData();
        // 通过userId获取user信息
        User user = userService.getById(userId);
        user.setAvatarUrl(avatarImageUrl);

        // 更新带有avatarUrl的user信息
        return userService.updateById(user) ?
                R.success(user) : R.error(CODE_311, CODE_311.getCodeMessage());
    }

    /**
     * 更新
     * @param user 新的用户信息
     * @return 是否更新成功
     */
    @PutMapping("/update")
    public R update(@RequestBody User user) {
        return userService.updateById(user) ?
                R.success(user) : R.error(CODE_310, CODE_310.getCodeMessage());
    }

//    @PutMapping("/update-col")
//    public R updateColumn(@RequestBody User user, ) {
//
//    }

    /**
     * 删除 ！！！
     * @param id 用户ID
     * @return ！！普通用户没有删除权限！！！
     */
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable Integer id) {
        return R.error(CODE_320, CODE_320.getCodeMessage());
    }
}
