package com.exercise.gbtrain.dto.priceadjustor.request;

import lombok.Data;

import java.util.List;

@Data
public class PriceAdjustorRequest {
    private String trainColor;
    private List<PriceAdjustorDetailRequest> priceAdjustorDetailRequests;
}
