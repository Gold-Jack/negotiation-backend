package com.negotiation.analysis.model;

import cn.hutool.core.util.StrUtil;
import com.hankcs.hanlp.HanLP;
import com.negotiation.common.util.QuestionType;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.negotiation.common.util.QuestionType.TEXT;

@Component
@NoArgsConstructor
public class TextAnalyzer implements Analyzer{

    private final QuestionType analyzerType = TEXT;

    @ApiOperation("简单文本分析，仅做关键词匹配，关键词手动设置")
    public static double simpleAnalyze(String keywords, String userAnswerText) {
        List<String> keywordList = StrUtil.split(keywords, StrUtil.COMMA);
        int hitCount = 0;
        for (String keyword : keywordList) {
            if (HanLP.segment(userAnswerText).contains(keyword)) {
                hitCount++;
            }
        }
        double simpleAnalysisScore = (1.0 * hitCount) / keywordList.size() * FULL_SCORE;
        return simpleAnalysisScore;
    }
}
