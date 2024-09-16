package com.exercise.gbtrain.controller;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.dto.farecalculator.request.FareRateRequest;
import com.exercise.gbtrain.service.FareCalculatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator")
public class FareCalculatorController {
    private final FareCalculatorService fareCalculatorService;

    public FareCalculatorController(FareCalculatorService fareCalculatorService) {
        this.fareCalculatorService = fareCalculatorService;
    }

    @GetMapping("/getcolormapping")
    public ResponseEntity<Object> getColorMapping(){
        var response = fareCalculatorService.getColorMappings();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getfarerate")
    public ResponseEntity<Object> getFareRate(@RequestBody FareRateRequest fareRateRequest){
        var response = fareCalculatorService.getFareRates(fareRateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/calculatefare")
    public ResponseEntity<Object> calculateFare(@RequestBody FareCalculatorRequest fareCalculatorRequest){
        var response = fareCalculatorService.calculateFare(fareCalculatorRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
