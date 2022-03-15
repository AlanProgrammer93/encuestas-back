package com.capitan.encuestas.models.requests;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class AnswerCreationRequestModel {
    
    @NotEmpty
    private String content;
}
