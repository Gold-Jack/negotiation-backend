package com.negotiation.user.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "User对象", description = "用户信息表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("已做过的测验ID")
    private String quizFinishedId;

    @ApiModelProperty("测验结果ID")
    private String quizResultId;

    @ApiModelProperty("用户权限")
    private String authority;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("职业")
    private String profession;

    @ApiModelProperty("登陆验证token")
    private String token;

    @ApiModelProperty("创建日期")
    private Date gmtCreate;

    @ApiModelProperty("修改日期")
    private Date gmtModified;

    @ApiModelProperty("逻辑删除")
    @TableLogic
    private Integer isDeleted;


}
