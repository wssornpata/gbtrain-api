package com.exercise.gbtrain.dto.farecalculator.response;

import lombok.Data;

@Data
public class CalculatedFareResponse {
    private String source;
    private String destination;
    private int distance;
    private float price;
}
