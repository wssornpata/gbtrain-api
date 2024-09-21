package com.exercise.gbtrain.dto.farecalculator.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculatedFareResponse {
    private String source;
    private String destination;
    private int distance;
    private float price;
    private int type;
}
