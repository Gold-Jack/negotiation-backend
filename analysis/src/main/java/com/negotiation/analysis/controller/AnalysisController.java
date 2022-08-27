package com.negotiation.analysis.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.negotiation.analysis.feign.QuestionFeignService;
import com.negotiation.analysis.feign.QuizFeignService;
import com.negotiation.analysis.feign.QuizResultFeignService;
import com.negotiation.analysis.model.Analyzer;
import com.negotiation.analysis.model.MultipleChoiceAnalyzer;
import com.negotiation.analysis.model.TextAnalyzer;
import com.negotiation.analysis.model.baidubce.LexicalAnalysis;
import com.negotiation.common.util.QuestionType;
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
    @Autowired
    private QuizResultFeignService quizResultFeignService;

    @ApiOperation("分析用户答案、计算得分[主方法]")
    @PostMapping("/do")
    public R doAnalysis(@RequestParam Integer quizId,
                        @RequestBody Map<String, String> userAnswer) {
        // 通过quizFeignService获取questionList
        List<Integer> questionIds = quizFeignService.getQuestionIds(quizId).getData();
        // 将userAnswer的题号从String转为Integer并存在answers这个新表中
        Map<Integer, String> answers = new HashMap<>();
        for (String k : userAnswer.keySet()) {
            answers.put(Integer.parseInt(k), userAnswer.get(k));
        }
        // 检查所有的userAnswer的题号是否都在questionList内
        Set<Integer> userAnswerQuestions = answers.keySet();
        if ( !new HashSet<>(questionIds).containsAll(userAnswerQuestions)) {
            // 如果不是
            return R.error(CODE_100, CODE_100.getCodeMessage().concat(": " + "题目缺失，无法判分！"));
        }

        // 初始化评分表
        Map<String, Double> scoreByAbility = new HashMap<>();
        scoreByAbility.put("expression-ability", 0.0);
        scoreByAbility.put("observation-ability", 0.0);
        scoreByAbility.put("negotiation-ability", 0.0);

        /*
        * TODO 按照question的type，创建不同的Analyzer，分别判分、求总分
        * */
        for (Integer questionId : answers.keySet()) {
            // 通过feign获取question对应的评分细则，这里暂且认定TEXT类型question存储了关键词
            double score = 0.0;
            String rule = questionFeignService.getRuleById(questionId).getData();
            String questionType = questionFeignService.getTypeById(questionId).getData();
            if (Objects.equals(questionType, QuestionType.TEXT.toString())) {
                // 使用文本分析器进行简单评分，传入关键词（rule）和用户的作答
                score = TextAnalyzer.simpleAnalyze(rule, answers.get(questionId));
            }
            if (StrUtil.equals(questionType, QuestionType.MULTIPLE_CHOICE.toString())) {
                score = MultipleChoiceAnalyzer.simpleAnalyze(rule, answers.get(questionId));
            }
            String judgingAbility = questionFeignService.getJudgingAbility(questionId).getData();
            double sumScore = scoreByAbility.get(judgingAbility) + score;
            scoreByAbility.put(judgingAbility, sumScore);
        }
        // 总分（negotiation-ability）是其他两个能力得分的平均
        scoreByAbility.put("negotiation-ability",
                (scoreByAbility.get("expression-ability") + scoreByAbility.get("observation-ability")) / 2);
        if (scoreByAbility.get("negotiation-ability") > 100.0) {
            System.out.println("总分超过100了: " + scoreByAbility.get("negotiation-ability"));
        }
        /*
        * TODO 创建一条quizResult记录，并存入数据库，将resultId返回
        * */
        Integer resultId = quizResultFeignService.persistence(quizId, scoreByAbility).getData();
        return R.success(resultId);
    }

}
