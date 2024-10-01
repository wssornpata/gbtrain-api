package com.exercise.gbtrain.dto.station.response;

import com.exercise.gbtrain.entity.StationEntity;
import lombok.Data;

@Data
public class StationListResponse {
    private int id;
    private String stationName;
    private String stationFullname;

    public static StationListResponse formStationEntity(StationEntity stationEntity) {
        StationListResponse response = new StationListResponse();
        response.setId(stationEntity.getId());
        response.setStationName(stationEntity.getStationName());
        response.setStationFullname(stationEntity.getStationFullname());
        return response;
    }
}
