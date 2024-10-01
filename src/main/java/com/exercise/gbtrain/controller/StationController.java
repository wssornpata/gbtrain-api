package com.exercise.gbtrain.controller;

import com.exercise.gbtrain.service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/station")
public class StationController {

    private Logger logger = LoggerFactory.getLogger(StationController.class);

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/getstation")
    public ResponseEntity<Object> getAllStation() {
        var response = stationService.getStationList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
