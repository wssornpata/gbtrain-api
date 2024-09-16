package com.exercise.gbtrain.dto.farecalculator.request;

import lombok.Data;

@Data
public class FareCalculatorRequest {
    String source;
    String destination;
    Integer type;
}
