package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.dto.farecalculator.request.FareRateRequest;
import com.exercise.gbtrain.dto.farecalculator.response.CalculatedFareResponse;
import com.exercise.gbtrain.dto.farecalculator.response.FareRateResponse;
import com.exercise.gbtrain.entity.ColorMappingEntity;
import com.exercise.gbtrain.entity.ComparatorEntity;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.entity.TransactionEntity;
import com.exercise.gbtrain.repository.ColorMappingRepository;
import com.exercise.gbtrain.repository.ComparatorRepository;
import com.exercise.gbtrain.repository.FareRateRepository;
import com.exercise.gbtrain.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<FareRateResponse> getFareRates(FareRateRequest fareRateRequest) {
        List<FareRateEntity> fareRateEntityList = fareRateRepository.findAllByTrainColorOrderByIdAsc(fareRateRequest.getTrainColor());
        return wrapperFareRateResponse(fareRateEntityList);
    }

    private List<FareRateResponse> wrapperFareRateResponse(List<FareRateEntity> fareRateEntityList) {
        List<FareRateResponse> responses = new ArrayList<>();
        for (FareRateEntity fareRateEntity : fareRateEntityList) {
            FareRateResponse response = new FareRateResponse();
            response.setDistance(fareRateEntity.getDistance());
            response.setPrice(fareRateEntity.getPrice());
            response.setDescription(fareRateEntity.getDescription());
            response.setUpdateDatetime(fareRateEntity.getUpdateDatetime());
        }
        return responses;
    }

    @Transactional
    public CalculatedFareResponse calculateFare(FareCalculatorRequest fareCalculatorRequest) {
        ComparatorEntity comparatorEntity = comparatorRepository.findOneBySourceAndDestination(
                fareCalculatorRequest.getSource(),
                fareCalculatorRequest.getDestination());

        if (comparatorEntity == null) {
            logger.error("Cant find Route path");
            comparatorEntity = new ComparatorEntity();
        }

        FareRateEntity fareRateEntity = fareRateRepository.findOneByTrainColorAndDistance(
                fareCalculatorRequest.getTrainColor(),
                comparatorEntity.getDistance());

        transactionRepository.saveAndFlush(wrapperTransactionEntity(fareCalculatorRequest, fareRateEntity));
        return wrapperCalculatedFareResponse(comparatorEntity, fareRateEntity);
    }

    private TransactionEntity wrapperTransactionEntity(FareCalculatorRequest fareCalculatorRequest, FareRateEntity fareRateEntity) {
        TransactionEntity entity = new TransactionEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setSource(fareCalculatorRequest.getSource());
        entity.setDestination(fareCalculatorRequest.getDestination());
        entity.setTrainColor(fareCalculatorRequest.getTrainColor());
        entity.setPrice(fareRateEntity.getPrice());
        entity.setType(fareCalculatorRequest.getType());
        entity.setCreateDatetime(now);
        return entity;
    }

    private CalculatedFareResponse wrapperCalculatedFareResponse(ComparatorEntity comparatorEntity, FareRateEntity fareRateEntity) {
        CalculatedFareResponse response = new CalculatedFareResponse();
        response.setSource(comparatorEntity.getSource());
        response.setDestination(comparatorEntity.getDestination());
        response.setDistance(comparatorEntity.getDistance());
        response.setPrice(fareRateEntity.getPrice());
        return response;
    }
}
