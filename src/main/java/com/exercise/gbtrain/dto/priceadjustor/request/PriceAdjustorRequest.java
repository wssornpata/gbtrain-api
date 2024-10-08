package com.exercise.gbtrain.dto.priceadjustor.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class PriceAdjustorRequest {

    @NotNull(message = "ID must not be null.")
    private Integer id;

    @NotNull(message = "Description must not be null.")
    @Size(max = 255, message = "Description must not exceed 255 characters.")
    private String description;

    @NotNull(message = "Distance must not be null.")
    private Integer distance;

    @NotNull(message = "Price must not be null.")
    @Min(message = "Price must greater than 0.0 .", value = 0L)
    private Float price;
}