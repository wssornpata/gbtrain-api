package com.exercise.gbtrain.dto.priceadjustor.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PriceAdjustorResponse {
    private List<PriceAdjustorDetailResponse> priceAdjustorDetailResponses;
    private String trainColor;
}
