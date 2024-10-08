package com.exercise.gbtrain.dto.farecalculator.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FareCalculatorRequest {

    @NotNull(message = "Source must not be null.")
    @Size(max = 5, message = "Source is invalid.")
    private String source;

    @NotNull(message = "Destination must not be null.")
    @Size(max = 5, message = "Source is invalid.")
    private String destination;
    
    @NotNull(message = "Type must not be null.")
    private Integer type;
}
