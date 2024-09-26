package com.exercise.gbtrain.dto.farecalculator.response;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculatedFareResponse {
    private String source;
    private String destination;
    private float price;
    private int type;

    public static CalculatedFareResponse wrapperCalculatedFareResponse(FareCalculatorRequest request, float price, int type) {
        return new CalculatedFareResponse(
                request.getSource(),
                request.getDestination(),
                price,
                type
        );
    }
}
