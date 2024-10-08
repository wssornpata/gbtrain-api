package com.exercise.gbtrain.controller;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.service.FareCalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculator")
public class FareCalculatorController {
    private final FareCalculatorService fareCalculatorService;

    public FareCalculatorController(FareCalculatorService fareCalculatorService) {
        this.fareCalculatorService = fareCalculatorService;
    }

    @PostMapping("/calculatefare")
    public ResponseEntity<Object> calculateFare(@RequestBody @Valid FareCalculatorRequest fareCalculatorRequest){
        var response = fareCalculatorService.calculateFare(fareCalculatorRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
