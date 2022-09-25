package com.negotiation.analysis;

import com.negotiation.analysis.model.TextAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TextAnalyzerTest {

    @Test
    public void simpleAnalyzeTest() {
        System.out.println(TextAnalyzerTest.class.getName() + ":");
        System.out.println("simpleAnalyze(\"健康, 苹果\", \"每天吃一个苹果，可以有效延长寿命，保持身体的健康。\") = "
                + TextAnalyzer.simpleAnalyze("健康, 苹果", "每天吃一个苹果，可以有效延长寿命，保持身体的健康。"));
        System.out.println("simpleAnalyze(\"利益权衡, 媒体\", \"首先，建筑公司...\") = "
                + TextAnalyzer.simpleAnalyze("利益权衡, 媒体", "首先，建筑公司抓到了一个重要时间点，即开张前夕，该时点出现任何问题对迪士尼来说都是一种损失；其次，他们利用弱势群体的身份率先占据了媒体理论至高点，让迪士尼公司在舆论处于下风，使得“公开澄清”这一对策变得无力；最后，他们拿出了在群众中煽动情绪的杀手锏——示威游行，让结果变得不可控，让迪士尼无论如何都无法全身而退。\n" +
                "\n" +
                "最后，我觉得这一切的根本原因，还是因为建筑公司清楚，迪士尼为了修建该主题公园已经花费了大量资金，沉没成本过于庞大，而未来收益可期，所以他们料定迪士尼会选择牺牲小一点的利益来保全更大的利益。"));
    }
}
