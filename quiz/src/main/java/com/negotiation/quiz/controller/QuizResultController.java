package com.negotiation.quiz.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.negotiation.common.util.R;
import com.negotiation.quiz.pojo.QuizResult;
import com.negotiation.quiz.service.IQuizResultService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 测试结果表
用于存放用户的测试结果 前端控制器
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@RestController
@RequestMapping("/quiz/quizResult")
public class QuizResultController {

    @Autowired
    private IQuizResultService quizResultService;

    @ApiOperation("通过resultId获取result信息")
    @GetMapping("/get/{resultId}")
    public R getById(@PathVariable Integer resultId) {
        QuizResult quizResultById = quizResultService.getById(resultId);
        return R.success(quizResultById);
    }

    @ApiOperation("创建一条新的quizResult记录，并存入数据库")
    @PostMapping("/persistence")
    public R<Integer> persistence(@RequestParam Integer quizId,
                         @RequestParam Map<String, Double> scoreByAbility) {
        // 设置quizResult的各项参数
        QuizResult quizResult = new QuizResult();
        // TODO 这种resultId生成方式是否合法
        quizResult.setResultId(Integer.valueOf(IdUtil.fastSimpleUUID()));
        quizResult.setQuizId(quizId);
        quizResult.setExpressionAbility(scoreByAbility.get("expression-ability"));
        quizResult.setObservationAbility(scoreByAbility.get("observation-ability"));
        quizResult.setNegotiationAbility(scoreByAbility.get("negotiation-ability"));
        quizResult.setGmtCreate(new Date());
        quizResult.setGmtModified(quizResult.getGmtCreate());

        quizResultService.save(quizResult);
        return R.success(quizResult.getResultId());
    }

}
