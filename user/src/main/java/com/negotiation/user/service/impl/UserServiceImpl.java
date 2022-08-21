package com.negotiation.user.service.impl;

import com.negotiation.user.pojo.User;
import com.negotiation.user.mapper.UserMapper;
import com.negotiation.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-17
 */
@Service
@Primary
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
