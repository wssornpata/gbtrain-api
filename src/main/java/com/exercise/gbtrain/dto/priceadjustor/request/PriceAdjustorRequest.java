package com.exercise.gbtrain.dto.priceadjustor.request;

import lombok.Data;

@Data
public class PriceAdjustorRequest {
    private String description;
    private int distance;
    private float price;
}
