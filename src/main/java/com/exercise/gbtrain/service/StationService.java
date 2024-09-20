package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.station.response.StationListResponse;
import com.exercise.gbtrain.entity.StationEntity;
import com.exercise.gbtrain.repository.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final Logger logger = LoggerFactory.getLogger(StationService.class);

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationListResponse> getStationList(){
        List<StationEntity> stationEntityList = stationRepository.findAllByOrderByIdAsc();
        return wrapperStationListResponse(stationEntityList);
    }

    private List<StationListResponse> wrapperStationListResponse(List<StationEntity> stationEntityList){
        return stationEntityList.stream().map(StationListResponse::formStationEntity).collect(Collectors.toList());
    }
}
