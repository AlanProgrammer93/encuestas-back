package com.capitan.encuestas.models.responses;

import java.util.List;

import lombok.Data;

@Data
public class PollRest {
    private long id;

    private String pollId;

    private String content;

    private boolean opened;

    private List<QuestionRest> questions;
}
