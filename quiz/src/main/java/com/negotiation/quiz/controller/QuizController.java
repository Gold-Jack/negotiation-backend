package com.negotiation.quiz.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.negotiation.common.util.R;
import com.negotiation.quiz.feign.QuestionFeignService;
import com.negotiation.quiz.pojo.Quiz;
import com.negotiation.quiz.service.IQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.negotiation.common.util.ResponseCode.CODE_314;

/**
 * <p>
 * 测试表：一个测试表中包含多道题目 前端控制器
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private IQuizService quizService;
    @Autowired
    private QuestionFeignService questionFeignService;


    /**
     * 分页读取quiz
     * @param pageNum 页码 默认：1
     * @param pageSize 每页大小 默认：4
     * @param search 搜索内容 默认：'' （暂时搜索quizname）
     * @return 页面信息
     */
    @GetMapping("/get-page")
    public R getByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "3") Integer pageSize,
                        @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Quiz> quizWrapper = Wrappers.<Quiz>lambdaQuery();
        // 如果搜索框内不为空
        if (StrUtil.isNotBlank(search)) {
            // 则添加到wrapper中作为查询条件
            quizWrapper.like(Quiz::getQuizname, search);
        }
        // 获取当前页面信息
        Page<Quiz> quizPage = quizService.page(new Page<Quiz>(pageNum, pageSize), quizWrapper);
        if (quizPage.getRecords() == null) {
            // 如果页面记录为空，则直接返回
            return R.success(quizPage);
        }
        // 如果不为空，将question对象列表一起返回
        quizPage.getRecords().forEach((quiz) -> {
            Object questionData = getQuestionListByQuizId(quiz.getQuizId()).getData();
            assert StrUtil.equals(questionData.getClass().getName(), "List<Object>");   // TODO 类型申明是否成立？
            quiz.setQuestionObjList((List<Object>) questionData);
        });
        return R.success(quizPage);
    }

    /**
     * 通过quizId获取所有的question对象
     * @param quizId
     * @return question对象列表
     */
    @GetMapping("/get-question-obj/{quizId}")
    public R getQuestionListByQuizId(@PathVariable Integer quizId) {
        Quiz quizById = quizService.getById(quizId);
        //
        Object questionData = getQuestionListByStrIdList(quizById.getQuestionIdList()).getData();
        assert StrUtil.equals(questionData.getClass().getName(), "List<Object>");   // TODO 类型申明是否成立？
        List<Object> questionObjList = (List<Object>) questionData;
        return R.success(questionObjList);
    }

    /**
     * 通过questionIdList获取question对象
     * @param questionIdList
     * @return question对象
     */
    @GetMapping("/get-question-obj")
    public R getQuestionListByStrIdList(@RequestParam String questionIdList) {
        // 从questionIdList中提取题号列表
        int[] questionIds = StrUtil.splitToInt(questionIdList, ",");
        List<Object> questionObjList = new ArrayList<Object>();
        Arrays.stream(questionIds).forEach(
                (questionId) -> {
                    // 遍历questionId，用过questionFeignService获取对应的question对象
                    Object question = questionFeignService.getById(questionId).getData();
                    // 存入questionObjList
                    questionObjList.add(question);
                }
        );
        return R.success(questionObjList);
    }

    /**
     * quiz对象持久化
     * @param quiz
     * @return
     */
    @PutMapping("/persistence")
    public R persistence(@RequestBody Quiz quiz) {
        if (quiz.getQuestionObjList() != null) {
            return R.error(CODE_314, CODE_314.getCodeMessage());
        }
        quiz.setQuestionObjList(null);
        quizService.save(quiz);     // TODO 不知道这里会有什么Error
        return R.success(quiz);
    }
}
