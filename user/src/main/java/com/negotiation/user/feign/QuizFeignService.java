package com.negotiation.user.feign;

import com.negotiation.common.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "quiz-service", contextId = "quizFeignClient")
public interface QuizFeignService {

    @ApiOperation("通过quizId获取quiz信息")
    @GetMapping("/quiz/get/{quizId}")
    public R getById(@PathVariable Integer quizId);

    @ApiOperation("分页获取quiz信息")
    @GetMapping("/quiz/get-page")
    public R getByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "3") Integer pageSize,
                       @RequestParam(defaultValue = "") String search);

    @ApiOperation("通过quizId获取quiz包含的题目信息")
    @GetMapping("/quiz/getQuizQuestion/{quizId}")
    public R getQuizQuestionById(@PathVariable Integer quizId);


    @ApiOperation("通过String类型的questionIdList，获取题目信息")
    @GetMapping("/quiz/getQuizQuestion")
    public R getQuizQuestion(@RequestParam String questionIdList);
}
