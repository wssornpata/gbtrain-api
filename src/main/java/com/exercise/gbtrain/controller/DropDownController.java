package com.exercise.gbtrain.controller;

import com.exercise.gbtrain.service.DropDownService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dropdown")
public class DropDownController {
    private final Logger logger = LoggerFactory.getLogger(DropDownController.class);
    private final DropDownService dropDownService;

    public DropDownController(DropDownService dropDownService) {
        this.dropDownService = dropDownService;
    }

    @GetMapping("/station")
    public ResponseEntity<Object> stationDropDown() {
        var response = dropDownService.getStationDropDown();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }@GetMapping("/type")
    public ResponseEntity<Object> typeDropDown() {
        var response = dropDownService.getTypeDropDown();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
