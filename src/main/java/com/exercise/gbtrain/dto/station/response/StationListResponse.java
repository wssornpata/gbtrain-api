package com.exercise.gbtrain.dto.station.response;

import com.exercise.gbtrain.entity.StationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class StationListResponse {
    private String trainColor;
    private List<StationEntity> stations;
}
