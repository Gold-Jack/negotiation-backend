package com.negotiation.analysis.feign;

import com.negotiation.common.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "quiz-service", contextId = "quizResultFeignClient")
public interface QuizResultFeignService {

    @ApiOperation("通过resultId获取result信息")
    @GetMapping("/quiz/quizResult/get/{resultId}")
    public R getById(@PathVariable Integer resultId);

    @ApiOperation("创建一条新的quizResult记录，并存入数据库")
    @PostMapping("/persistence")
    public R<Integer> persistence(@RequestParam Integer quizId,
                                  @RequestParam Map<String, Double> scoreByAbility);
}
