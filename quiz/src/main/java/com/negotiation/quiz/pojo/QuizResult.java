package com.negotiation.quiz.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 测试结果表
用于存放用户的测试结果
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("quiz_result")
@ApiModel(value = "QuizResult对象", description = "测试结果表")
public class QuizResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测试结果的ID")
      @TableId(value = "result_id", type = IdType.AUTO)
    private Integer resultId;

    @ApiModelProperty("测试结果所对应的测试的ID号")
    private Integer quizId;

    @ApiModelProperty("表达能力：用于衡量用户陈述观点的能力")
    private Double expressionAbility;

    @ApiModelProperty("观察能力（阅读能力）：用于衡量用户观察对方言谈举止的能力")
    private Double observationAbility;

    @ApiModelProperty("谈判能力（总分）：用于衡量用户整体的谈判能力")
    private Double negotiationAbility;

    @ApiModelProperty("测试结果生成的日期")
    private Date gmtCreate;

    @ApiModelProperty("测试结果被修改或更新的日期")
    private Date gmtModified;

    @ApiModelProperty("逻辑删除")
    private Integer isDeleted;


}
