package com.capitan.encuestas.models.responses;

import java.util.List;

import com.capitan.encuestas.enums.QuestionType;

import lombok.Data;

@Data
public class QuestionRest {
    private long id;

    private String content;

    private int questionOrder;

    private QuestionType type;

    private List<AnswerRest> answers;
}
