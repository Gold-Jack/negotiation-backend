package com.negotiation.quiz.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 测试表
一个测试表中包含多道题目
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Quiz对象", description = "测试表")
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测试ID")
      @TableId(value = "quiz_id", type = IdType.AUTO)
    private Integer quizId;

    @ApiModelProperty("测试的名称")
    private String quizname;

    @ApiModelProperty("测试的描述、概要")
    private String description;

    @ApiModelProperty("题目ID列表")
    private String questionIdList;

    @TableField(exist = false)
    @ApiModelProperty("题目对象列表（不存入数据库）")
    private List<Object> questionObjList;

    @ApiModelProperty("已参加测试的人数")
    private Integer quizDoneNumber;

    @ApiModelProperty("测试创建人")
    private String creator;

    @ApiModelProperty("测试发布人")
    private String publisher;

    @ApiModelProperty("测试发布日期")
    private Date gmtPublish;

    @ApiModelProperty("测试预计时长（单位：分钟）")
    private Integer estimatedTimeCost;

    @ApiModelProperty("测试是否被发布")
    private Integer isPublish;

    @ApiModelProperty("测试是否被下架")
    private Integer isOff;

    @ApiModelProperty("测试创建日期")
    private Date gmtCreate;

    @ApiModelProperty("测试被修改或更新的日期")
    private Date gmtModified;

    @ApiModelProperty("逻辑删除")
    @TableLogic
    private Integer isDeleted;


}
