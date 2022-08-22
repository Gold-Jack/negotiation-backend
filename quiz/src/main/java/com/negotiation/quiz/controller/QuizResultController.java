package com.negotiation.quiz.controller;

import com.negotiation.common.util.R;
import com.negotiation.quiz.pojo.QuizResult;
import com.negotiation.quiz.service.IQuizResultService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

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
}
