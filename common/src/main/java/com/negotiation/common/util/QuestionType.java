package com.negotiation.common.util;

import lombok.Getter;

@Getter
public enum QuestionType {
    MULTIPLE_CHOICE("单项选择题"),
    TEXT("文本作答题"),
    ;

    private final String questionTypeName;

    QuestionType(String questionTypeName) {
        this.questionTypeName = questionTypeName;
    }
}
