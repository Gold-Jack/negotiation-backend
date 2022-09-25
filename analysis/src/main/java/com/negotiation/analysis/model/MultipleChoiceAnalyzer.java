package com.negotiation.analysis.model;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@NoArgsConstructor
public class MultipleChoiceAnalyzer implements Analyzer{

    @ApiOperation("简单单项选择评分，只有一个正确选项，如果用户答对（满分），答错（零分）")
    public static Double simpleAnalyze(String correctChoice, String userChoice) {
        correctChoice = correctChoice.toUpperCase(Locale.ENGLISH).strip();
        userChoice = userChoice.toUpperCase(Locale.ENGLISH).strip();
        // 申明答案和用户作答的选项的数量均为1
        assert correctChoice.length() == 1 && userChoice.length() == 1;
        if (StrUtil.equals(correctChoice, userChoice)) {
            return FULL_SCORE;
        }

        return ZERO;
    }
}
