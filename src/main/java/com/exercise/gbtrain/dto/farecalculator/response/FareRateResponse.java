package com.exercise.gbtrain.dto.farecalculator.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FareRateResponse {
    private int distance;
    private float price;
    private String description;
    private LocalDateTime updateDatetime;
}
