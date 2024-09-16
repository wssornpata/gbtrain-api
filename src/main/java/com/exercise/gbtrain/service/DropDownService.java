package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.station.response.StationListResponse;
import com.exercise.gbtrain.repository.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropDownService {
    private final Logger logger = LoggerFactory.getLogger(DropDownService.class);
    private final StationService stationService;

    public DropDownService(StationService stationService) {
        this.stationService = stationService;
    }

    public List<StationListResponse> getDropDown(){
        logger.info("DropdownService: dropdown");
        return stationService.getStationList();
    }
}
