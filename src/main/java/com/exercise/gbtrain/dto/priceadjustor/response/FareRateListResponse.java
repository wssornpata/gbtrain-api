package com.exercise.gbtrain.dto.priceadjustor.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FareRateListResponse {
    private int id;
    private int distance;
    private float price;
    private LocalDateTime updateDateTime;
}
