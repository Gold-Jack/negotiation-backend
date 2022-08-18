package com.negotiation.question.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 题目表
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@Getter
@Setter
@ApiModel(value = "Question对象", description = "题目表")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("题目ID")
      @TableId(value = "question_id", type = IdType.AUTO)
    private Integer questionId;

    @ApiModelProperty("题目类型")
    private String type;

    @ApiModelProperty("本题评价的能力")
    private String judgingAbility;

    @ApiModelProperty("题目描述")
    private String description;

    @ApiModelProperty("题目内容（文件）以url的形式存储在数据库中")
    private String contentUrl;

    @ApiModelProperty("评分规则 - 当前版本只给一个选项或关键词")
    private String rule;

    @ApiModelProperty("题目创建人")
    private String creator;

    @ApiModelProperty("题目发布人")
    private String publisher;

    @ApiModelProperty("创建日期")
    private Date gmtCreate;

    @ApiModelProperty("修改日期")
    private Date gmtModified;

    @ApiModelProperty("逻辑删除")
    private Integer isDeleted;


}
