package com.exercise.gbtrain.dto.farecalculator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class FareCalculatorDistanceMap {
    private int distance;
    private Map<String, Integer> distanceMap;

}
