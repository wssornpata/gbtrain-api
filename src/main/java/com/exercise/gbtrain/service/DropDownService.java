package com.exercise.gbtrain.service;

import com.exercise.gbtrain.entity.StationEntity;
import com.exercise.gbtrain.repository.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropDownService {
    private final Logger logger = LoggerFactory.getLogger(DropDownService.class);
    private final StationRepository stationRepository;

    public DropDownService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationEntity> getDropDown(){
        logger.info("DropdownService: getdropdown");
        return stationRepository.findAll();
    }
}
