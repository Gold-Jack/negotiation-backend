package com.negotiation.analysis;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.hankcs.hanlp.HanLP;
import com.negotiation.analysis.model.baidubce.LexicalAnalysis;
import com.negotiation.analysis.model.baidubce.ShortTextSimilarity;
import com.negotiation.common.util.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
public class BaidubceApplicationTest {

    @Test
    public void lexicalAnalysisTest() {
        // 中文乱码问题已经解决，需要更改junit的java版本与项目版本一致
        String text = "百度是一家低科技公司，但他提供的bce服务还挺不错的";
        System.out.println("LexicalAnalysis = " + StrUtil.toString(
                LexicalAnalysis.analyze(text).getResult())
        );
    }

    @Test
    public void ShortTextSimilarityTest() {
        String text_1 = "工人工资确实是迪斯尼的问题，而且此时迪士尼公司付工资的损失更小";
        String text_2 = "迫使迪士尼公司进行利益权衡，索要款项是正当的，同时利用了媒体的力量";
        List<Character> toRemoveCharacters = new ArrayList<>();
        toRemoveCharacters.add(CharUtil.BRACKET_START);
        toRemoveCharacters.add(CharUtil.BRACKET_END);
        String text_1_keywords = CommonUtil.removeAllChar(HanLP.extractKeyword(text_1, 4).toString(), toRemoveCharacters);
        String text_2_keywords = CommonUtil.removeAllChar(HanLP.extractKeyword(text_2, 4).toString(), toRemoveCharacters);
        System.out.println("ShortTextSimilarity = " + ShortTextSimilarity.analyze(text_1_keywords, text_2_keywords));
    }

    @Test
    public void testRemoveChar() {
        String str = "\"This quote.\"";
        List<String> listStr = new ArrayList<>();
        listStr.add("a");
        listStr.add("b");
        listStr.forEach((s) -> s = s.concat("Concat"));
        System.out.println(listStr);
        System.out.println("str = " + CommonUtil.removeChar(str, CharUtil.DOUBLE_QUOTES));
    }

    @Test
    public void encodingTest() {
        System.out.println(System.getProperty("file.encoding"));
//        System.out.println(System.getProperty("console.encoding"));
        System.out.println("空的，中文乱码");
    }
}
