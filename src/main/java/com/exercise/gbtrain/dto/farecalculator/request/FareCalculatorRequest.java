package com.exercise.gbtrain.dto.farecalculator.request;

import lombok.Data;

@Data
public class FareCalculatorRequest {
    private String source;
    private String destination;
    private Integer type;
}
