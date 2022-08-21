package com.negotiation.user.controller;

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
