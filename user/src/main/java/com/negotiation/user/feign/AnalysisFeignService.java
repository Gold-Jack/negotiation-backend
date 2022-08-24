package com.negotiation.user.feign;

import com.negotiation.common.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("analysis-service")
public interface AnalysisFeignService {

    @ApiOperation("分析用户答案、计算得分[主方法]")
    @PostMapping("/analysis/do")
    public R doAnalysis(@RequestParam Integer quizId,
                        @RequestBody Map<String, String> userAnswer);
}
