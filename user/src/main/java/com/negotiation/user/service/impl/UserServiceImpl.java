package com.negotiation.user.service.impl;

import com.negotiation.common.util.CommonUtil;
import com.negotiation.user.pojo.User;
import com.negotiation.user.mapper.UserMapper;
import com.negotiation.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public User getUserById(Integer userId) {
        User userById = getById(userId);
        if (userById == null) {
            return null;
        }
        /*
         * 更新quizFinishedIdHash
         * */
        userById.setQuizFinishedIdHash(CommonUtil.stringToHash(userById.getQuizFinishedId()));
        /*
         * 更新quizResultIdHash
         * */
        userById.setQuizResultIdHash(CommonUtil.stringToHash(userById.getQuizResultId()));

        return userById;
    }

    @Override
    public boolean updateUser(User user) {
        user.setQuizFinishedId(CommonUtil.hashToString(user.getQuizFinishedIdHash()));
        user.setQuizResultId(CommonUtil.hashToString(user.getQuizResultIdHash()));
        return updateById(user);
    }

}
