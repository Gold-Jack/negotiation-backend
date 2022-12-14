package com.negotiation.quiz.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.negotiation.common.util.CommonUtil;
import com.negotiation.common.util.R;
import com.negotiation.quiz.feign.QuestionFeignService;
import com.negotiation.quiz.pojo.Quiz;
import com.negotiation.quiz.service.IQuizService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.negotiation.common.util.ResponseCode.CODE_311;
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

    @ApiOperation("通过quizId获取quiz信息")
    @GetMapping("/get/{quizId}")
    public R getById(@PathVariable Integer quizId) {
        Quiz quizById = quizService.getById(quizId);
        if (quizById == null) {
            return R.error(CODE_311, CODE_311.getCodeMessage());
        }
        List<Object> questionObjList = new ArrayList<>();
        CommonUtil.stringToHash(quizById.getQuestionIdList())
                .stream().iterator().forEachRemaining((id) -> {
                    questionObjList.add(getQuizQuestionById(id).getData());
                });
        quizById.setQuestionObjList(questionObjList);
        return R.success(quizById);
    }

    @ApiOperation("获取单个quiz的具体信息（前端调用）")
    @GetMapping("/getQuizDetail")
    public R getQuizDetail(@RequestParam Integer quizId) {
        return getById(quizId);
    }

    @ApiOperation("获取所有已发布的quiz")
    @GetMapping("/getAllPublishedQuiz")
    public R getAllPublishedQuiz() {
        final Integer IS_PUBLISH = 1;
        List<Quiz> publishedQuizList = quizService.list(
                Wrappers.<Quiz>lambdaQuery().eq(Quiz::getIsPublish, IS_PUBLISH));
        return R.success(publishedQuizList);
    }


    /**
     * 分页读取quiz
     * @param pageNum 页码 默认：1
     * @param pageSize 每页大小 默认：4
     * @param search 搜索内容 默认：'' （暂时搜索quizname）
     * @return 页面信息
     */
    @ApiOperation("分页获取quiz信息")
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
            Object questionData = getQuizQuestionById(quiz.getQuizId()).getData();
            assert StrUtil.equals(questionData.getClass().getName(), List.class.getName());   // TODO 类型申明是否成立？
            quiz.setQuestionObjList((List<Object>) questionData);
        });
        return R.success(quizPage);
    }

    /**
     * 通过quizId获取所有的question对象
     * @param quizId
     * @return question对象列表
     */
    @ApiOperation("通过quizId获取quiz包含的题目信息")
    @GetMapping("/getQuizQuestion/{quizId}")
    public R getQuizQuestionById(@PathVariable Integer quizId) {
        Quiz quizById = quizService.getById(quizId);
        return getQuizQuestion(quizById.getQuestionIdList());
    }

    /**
     * 通过questionIdList获取question对象
     * @param questionIdList
     * @return question对象
     */
    @ApiOperation("通过String类型的questionIdList，获取题目信息")
    @GetMapping("/getQuizQuestion")
    public R getQuizQuestion(@RequestParam String questionIdList) {
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

    @ApiOperation("获取quiz包含的所有question的id")
    @GetMapping("/getQuestionIds")
    public R<List<Integer>> getQuestionIds(@RequestParam Integer quizId) {
        Quiz quiz = quizService.getById(quizId);
        int[] questions = StrUtil.splitToInt(quiz.getQuestionIdList(), ",");
        List<Integer> questionIds = ListUtil.toList(Arrays.stream(questions).iterator());
        return R.success(questionIds);
    }

    /**
     * quiz对象持久化
     * @param quiz
     * @return
     */
    @ApiOperation("quiz信息持久化")
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
