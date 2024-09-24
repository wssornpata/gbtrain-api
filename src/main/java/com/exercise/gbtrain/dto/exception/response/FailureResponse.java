package com.exercise.gbtrain.dto.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailureResponse {
    private String message;
}