package com.exercise.gbtrain.dto.priceadjustor.response;

import lombok.Data;

import java.util.List;

@Data
public class PriceAdjustorResponse {
    private String trainColor;
    private List<PriceAdjustorDetailResponse> priceAdjustorDetailResponses;
}
