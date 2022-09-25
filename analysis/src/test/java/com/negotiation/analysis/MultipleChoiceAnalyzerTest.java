package com.negotiation.analysis;

import com.negotiation.analysis.model.MultipleChoiceAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MultipleChoiceAnalyzerTest {

    @Test
    public void simpleAnalyzeTest() {
        System.out.println(MultipleChoiceAnalyzerTest.class.getName() + ":");
        System.out.println("simpleAnalyze(\"C\", \"D\") = " + MultipleChoiceAnalyzer.simpleAnalyze("C", "D"));
        System.out.println("simpleAnalyze(\"A\", \" A \") = " + MultipleChoiceAnalyzer.simpleAnalyze("A", " A "));
        System.out.println("simpleAnalyze(\" A      \", \"a\") = " + MultipleChoiceAnalyzer.simpleAnalyze(" A      ", "a"));
        System.out.println("simpleAnalyze(\"B\", \"B\") = " + MultipleChoiceAnalyzer.simpleAnalyze("B", "B"));
    }
}
