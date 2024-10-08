package com.exercise.gbtrain.controller;

import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.service.PriceAdjustorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priceadjust")
public class PriceAdjustorController {
    private final Logger logger = LoggerFactory.getLogger(PriceAdjustorController.class);

    private final PriceAdjustorService priceAdjustorService;

    public PriceAdjustorController(PriceAdjustorService priceAdjustorService) {
        this.priceAdjustorService = priceAdjustorService;
    }

    @GetMapping("/getfarerate")
    public ResponseEntity<Object> getfarerate() {
        var response = priceAdjustorService.getfarerate();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/adjustprice")
    public ResponseEntity<Object> adjustPrice(@RequestBody @Valid List<PriceAdjustorRequest> priceAdjustorRequestList) {
        var response = priceAdjustorService.adjustPrice(priceAdjustorRequestList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
