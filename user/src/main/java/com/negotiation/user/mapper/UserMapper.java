package com.negotiation.user.mapper;

import com.negotiation.user.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-17
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user")
    List<User> mySqlTest();

}
