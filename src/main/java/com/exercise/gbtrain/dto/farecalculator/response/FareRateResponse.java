package com.exercise.gbtrain.dto.farecalculator.response;

import lombok.Data;

@Data
public class FareRateResponse {
    private int rate_id;
    private int gap;
    private FareRateDetailResponse detail;
}
