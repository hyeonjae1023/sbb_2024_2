package com.org.sbb2.question;

import lombok.Getter;

@Getter
public enum QuestionEnum {
    QNA(0),
    FREE(1),
    BUG(2);

    private Integer status;

    QuestionEnum(Integer status) {
        this.status = status;
    }
}
