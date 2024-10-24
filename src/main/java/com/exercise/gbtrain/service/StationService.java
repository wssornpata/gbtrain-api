package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.station.response.StationListResponse;
import com.exercise.gbtrain.entity.StationEntity;
import com.exercise.gbtrain.repository.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final Logger logger = LoggerFactory.getLogger(StationService.class);

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

//    @Transactional(readOnly = true)
//    public List<StationListResponse> getStationList() {
//        List<StationEntity> stationEntityList = stationRepository.findAllByOrderByIdAsc();
//        return wrapperStationListResponses(stationEntityList);
//    }

    @Transactional(readOnly = true)
    public List<StationEntity> getStationEntityList() {
        List<StationEntity> stationEntityList = stationRepository.findAllByOrderByIdAsc();
        return stationEntityList;
    }

//    public List<StationListResponse> wrapperStationListResponses(List<StationEntity> stationEntities) {
//
//        Map<String, List<StationEntity>> stationsByColor = stationEntities.stream()
//                .collect(Collectors.groupingBy(station -> station.getColorMappingEntity().getColorName()));
//
//        logger.info(stationsByColor.toString());
//
//        return stationsByColor.entrySet().stream()
//                .map(entry -> new StationListResponse(entry.getKey(), entry.getValue()))
//                .collect(Collectors.toList());
//    }

//    private List<StationListResponse> wrapperStationListResponses(List<StationEntity> stationEntityList) {
//        return stationEntityList.stream().map(StationListResponse::getStations).collect(Collectors.toList());
//    }
}
