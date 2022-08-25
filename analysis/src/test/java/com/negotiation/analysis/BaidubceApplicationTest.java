package com.negotiation.analysis;

import cn.hutool.core.util.CharUtil;
import com.negotiation.analysis.model.baidubce.LexicalAnalysis;
import com.negotiation.common.util.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BaidubceApplicationTest {

    @Test
    public void testLexicalAnalysis() {
        // TODO 解决中文乱码问题
//        System.out.println("BaiduAccessToken.getToken() = " + CommonUtil.baiduJsonToMap(LexicalAnalysis.forward()));
        System.out.println("LexicalAnalysis.f\\ = " + LexicalAnalysis.forward().getResult());
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
}
