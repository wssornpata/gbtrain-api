package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.dto.farecalculator.response.CalculatedFareResponse;
import com.exercise.gbtrain.dto.farecalculator.response.FareRateDetailResponse;
import com.exercise.gbtrain.dto.farecalculator.response.FareRateResponse;
import com.exercise.gbtrain.entity.ColorMappingEntity;
import com.exercise.gbtrain.entity.ComparatorEntity;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.repository.ColorMappingRepository;
import com.exercise.gbtrain.repository.ComparatorRepository;
import com.exercise.gbtrain.repository.FareRateRepository;
import com.exercise.gbtrain.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FareCalculatorService {
    private final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);
    private final ColorMappingRepository colorMappingRepository;
    private final FareRateRepository fareRateRepository;
    private final TransactionRepository transactionRepository;
    private final ComparatorRepository comparatorRepository;

    public FareCalculatorService(ColorMappingRepository colorMappingRepository, FareRateRepository fareRateRepository, TransactionRepository transactionRepository, ComparatorRepository comparatorRepository) {
        this.colorMappingRepository = colorMappingRepository;
        this.fareRateRepository = fareRateRepository;
        this.transactionRepository = transactionRepository;
        this.comparatorRepository = comparatorRepository;
    }

    public List<ColorMappingEntity> getColorMappings() {
        return colorMappingRepository.findAll();
    }

    public  List<FareRateResponse>getFareRates() {
        List<FareRateEntity> fareRateEntityList =  fareRateRepository.findAllByTrainColorOrderByIdAsc("Green");
        for(FareRateEntity fareRateEntity : fareRateEntityList){
            logger.info(fareRateEntity.toString());
        }

        fareRateEntityList.clear();
        return new ArrayList<>();
    }

    private FareRateResponse wrapperFareRateResponse(FareRateEntity fareRateEntity) {
        return null;
    }

    public CalculatedFareResponse calculateFare(FareCalculatorRequest fareCalculatorRequest) {
        String source = fareCalculatorRequest.getSource();
        String destination = fareCalculatorRequest.getDestination();
        ComparatorEntity comparatorEntity =  comparatorRepository.findOneBySourceAndDestination(source, destination);

        int price = 200;
        if(comparatorEntity == null){
            price = 0;
        }

        return wrapperCalculatedFareResponse(comparatorEntity, price);
    }

    private CalculatedFareResponse wrapperCalculatedFareResponse(ComparatorEntity comparatorEntity,int price) {
        CalculatedFareResponse response = new CalculatedFareResponse();
        response.setSource(comparatorEntity.getSource());
        response.setDestination(comparatorEntity.getDestination());
        response.setDistance(comparatorEntity.getDistance());
        response.setPrice(price);
        return response;
    }
}
