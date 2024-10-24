package com.exercise.gbtrain.dto.farecalculator.response;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculatedFareResponse {
    private String origin;
    private String destination;
    private float price;
    private String type;

    public static CalculatedFareResponse wrapperCalculatedFareResponse(FareCalculatorRequest request, float price, String type) {
        return new CalculatedFareResponse(
                request.getOrigin(),
                request.getDestination(),
                price,
                type
        );
    }
}
