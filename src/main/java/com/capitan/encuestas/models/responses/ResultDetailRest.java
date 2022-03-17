package com.capitan.encuestas.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultDetailRest {
    private String answer;
    private long result;
}
