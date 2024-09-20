package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.station.response.StationListResponse;
import com.exercise.gbtrain.dto.type.response.TypeListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropDownService {
    private final Logger logger = LoggerFactory.getLogger(DropDownService.class);
    private final StationService stationService;
    private final TypeService typeService;

    public DropDownService(StationService stationService, TypeService typeService) {
        this.stationService = stationService;
        this.typeService = typeService;
    }

    public List<StationListResponse> getStationDropDown() {
        logger.info("DropdownService: getStationDropDown");
        return stationService.getStationList();
    }

    public List<TypeListResponse> getTypeDropDown() {
        logger.info("DropdownService: getTypeDropDown");
        return typeService.getTypeList();
    }
}
