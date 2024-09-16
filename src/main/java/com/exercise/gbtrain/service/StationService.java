package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.station.TrainStationRequest;
import com.exercise.gbtrain.dto.station.response.StationListResponse;
import com.exercise.gbtrain.entity.StationEntity;
import com.exercise.gbtrain.repository.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<StationListResponse> getStationListByTrainColor(TrainStationRequest trainStationRequest){
        List<StationEntity> stationEntityList = stationRepository.findAllByTrainColorOrderByIdAsc(trainStationRequest.getTrainColor());
        return wrapperStationListResponse(stationEntityList);
    }

    private List<StationListResponse> wrapperStationListResponse(List<StationEntity> stationEntityList){
        List<StationListResponse> responseList = new ArrayList<>();
        for(StationEntity stationEntity : stationEntityList){
            StationListResponse response = new StationListResponse();
            response.setId(stationEntity.getId());
            response.setStationName(stationEntity.getStationName());
            response.setStationFullname(stationEntity.getStationFullname());
            responseList.add(response);
        }
        return responseList;
    }
}
