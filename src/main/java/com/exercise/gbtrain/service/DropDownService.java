package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.station.response.StationListResponse;
import com.exercise.gbtrain.entity.ColorMappingEntity;
import com.exercise.gbtrain.entity.StationEntity;
import com.exercise.gbtrain.repository.ColorMappingRepository;
import com.exercise.gbtrain.repository.StationRepository;
import com.exercise.gbtrain.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropDownService {
    private final Logger logger = LoggerFactory.getLogger(DropDownService.class);
    private final StationService stationService;
    private final StationRepository stationRepository;

    public DropDownService(StationService stationService, StationRepository stationRepository) {
        this.stationService = stationService;
        this.stationRepository = stationRepository;
    }

    public List<StationListResponse> getDropDown(){
        logger.info("DropdownService: dropdown");
        return stationService.getStationList();
    }
}
