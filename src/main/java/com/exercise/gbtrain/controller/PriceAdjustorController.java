package com.exercise.gbtrain.controller;

import com.exercise.gbtrain.dto.priceadjustor.request.GetPriceAdjustorRequest;
import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.service.PriceAdjustorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/priceadjust")
public class PriceAdjustorController {
    private final Logger logger = LoggerFactory.getLogger(PriceAdjustorController.class);

    private final PriceAdjustorService priceAdjustorService;

    public PriceAdjustorController(PriceAdjustorService priceAdjustorService) {
        this.priceAdjustorService = priceAdjustorService;
    }

    @GetMapping ("/getprice")
    public ResponseEntity<Object> getPrice(@RequestBody GetPriceAdjustorRequest getPriceAdjustorRequest) {
        var response = priceAdjustorService.getPrice(getPriceAdjustorRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/createprice")
    public ResponseEntity<Object> createPrice(@RequestBody PriceAdjustorRequest priceAdjustorRequest) {
        var response = priceAdjustorService.createPrice(priceAdjustorRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/adjustprice")
    public ResponseEntity<Object> adjustPrice(@RequestBody PriceAdjustorRequest priceAdjustorRequest) {
        var response = priceAdjustorService.adjustPrice(priceAdjustorRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
