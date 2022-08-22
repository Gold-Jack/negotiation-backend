package com.negotiation.analysis.controller;

import com.negotiation.analysis.feign.QuestionFeignService;
import com.negotiation.analysis.feign.QuizFeignService;
import com.negotiation.common.util.R;
import com.negotiation.common.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.negotiation.common.util.ResponseCode.CODE_100;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private QuestionFeignService questionFeignService;
    @Autowired
    private QuizFeignService quizFeignService;

    @PostMapping("/do")
    public R doAnalysis(@RequestParam Integer quizId,
                        @RequestParam Map<Integer, String> userAnswer) {
        // 通过quizFeignService获取questionList
        List<Integer> questionIds = quizFeignService.getQuestionIds(quizId).getData();
        // 检查所有的userAnswer的题号是否都在questionList内
        if ( !new HashSet<>(questionIds).containsAll(userAnswer.keySet())) {
            // 如果不是
            return R.error(CODE_100, CODE_100.getCodeMessage().concat("\n" + "题目缺失，无法判分！"));
        }
        /*
        * TODO 按照question的type，创建不同的Analyzer，分别判分、求总分
        * */
        return R.error(CODE_100, "方法未完成");
    }
}
