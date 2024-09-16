package com.exercise.gbtrain.dto.farecalculator.response;

import lombok.Data;

@Data
public class FareRateDetailResponse {
    private int rate_id;
    private int distance;
    private float price;
}
