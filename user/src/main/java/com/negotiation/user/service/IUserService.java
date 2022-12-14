package com.negotiation.user.service;

import com.negotiation.user.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-17
 */
public interface IUserService extends IService<User> {

    public User getUserById(Integer userId);

    public boolean updateUser(User user);

}
