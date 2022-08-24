package com.negotiation.analysis.controller;

import cn.hutool.core.util.StrUtil;
import com.negotiation.analysis.feign.QuestionFeignService;
import com.negotiation.analysis.feign.QuizFeignService;
import com.negotiation.common.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.negotiation.common.util.ResponseCode.CODE_100;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private QuestionFeignService questionFeignService;
    @Autowired
    private QuizFeignService quizFeignService;

    @ApiOperation("分析用户答案、计算得分[主方法]")
    @PostMapping("/do")
    public R doAnalysis(@RequestParam Integer quizId,
                        @RequestBody Map<String, String> userAnswer) {
        // 通过quizFeignService获取questionList
        List<Integer> questionIds = quizFeignService.getQuestionIds(quizId).getData();
        // 检查所有的userAnswer的题号是否都在questionList内
        Set<Integer> userAnswerQuestions = userAnswer.keySet().stream().map(
                (str) -> Integer.parseInt(str)
        ).collect(Collectors.toSet());
        if ( !new HashSet<>(questionIds).containsAll(userAnswerQuestions)) {
            // 如果不是
            return R.error(CODE_100, CODE_100.getCodeMessage().concat(": " + "题目缺失，无法判分！"));
        }
        /*
        * TODO 按照question的type，创建不同的Analyzer，分别判分、求总分
        * */
        final Integer resultId = 1001;
        return R.success(resultId);
    }
}
