package com.exercise.gbtrain.dto.farecalculator.response;

import com.exercise.gbtrain.entity.StationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculatedFareResponse {
	private String origin;
	private String originFullname;
	private String destination;
	private String destinationFullname;
	private float price;
	private String type;

	public static CalculatedFareResponse wrapperCalculatedFareResponse(StationEntity originStationEntity, StationEntity destinationEntity, float price, String type) {
		return new CalculatedFareResponse(
				originStationEntity.getStationName(),
				originStationEntity.getStationFullname(),
				destinationEntity.getStationName(),
				destinationEntity.getStationFullname(),
				price,
				type
		);
	}
}
