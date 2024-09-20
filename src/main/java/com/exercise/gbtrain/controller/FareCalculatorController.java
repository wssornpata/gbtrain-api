package com.exercise.gbtrain.controller;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.service.FareCalculatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/calculator")
public class FareCalculatorController {
    private final FareCalculatorService fareCalculatorService;

    public FareCalculatorController(FareCalculatorService fareCalculatorService) {
        this.fareCalculatorService = fareCalculatorService;
    }

    @GetMapping("/getfarerate")
    public ResponseEntity<Object> getFareRate(){
        var response = fareCalculatorService.getFareRates();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/calculatefare")
    public ResponseEntity<Object> calculateFare(@RequestBody FareCalculatorRequest fareCalculatorRequest){
        var response = fareCalculatorService.calculateFare(fareCalculatorRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
