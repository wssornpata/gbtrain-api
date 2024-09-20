package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.dto.farecalculator.response.CalculatedFareResponse;
import com.exercise.gbtrain.dto.farecalculator.response.FareRateResponse;
import com.exercise.gbtrain.entity.ExtendMappingEntity;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.entity.TransactionEntity;
import com.exercise.gbtrain.repository.ExtendMappingRepository;
import com.exercise.gbtrain.repository.FareRateRepository;
import com.exercise.gbtrain.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FareCalculatorService {
    private final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);
    private final FareRateRepository fareRateRepository;
    private final TransactionRepository transactionRepository;
    private final ExtendMappingRepository extendMappingRepository;

    private final GraphService graphService;

    public FareCalculatorService(FareRateRepository fareRateRepository, TransactionRepository transactionRepository, ExtendMappingRepository extendMappingRepository, GraphService graphService) {
        this.fareRateRepository = fareRateRepository;
        this.transactionRepository = transactionRepository;
        this.extendMappingRepository = extendMappingRepository;
        this.graphService = graphService;
    }

    @Transactional(readOnly = true)
    public List<FareRateResponse> getFareRates() {
        logger.info("FareCalculatorService: getFareRates");
        List<FareRateEntity> fareRateEntityList = fareRateRepository.findAllByOrderByIdAsc();
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
            responses.add(response);
        }
        return responses;
    }

    @Transactional (readOnly = true)
    public CalculatedFareResponse calculateFare(FareCalculatorRequest fareCalculatorRequest) {
        validateFareCalculatorRequest(fareCalculatorRequest);

        String source = fareCalculatorRequest.getSource();
        String destination = fareCalculatorRequest.getDestination();

        int distanceSourceToDestination = graphService.getDistance(source, destination);

        if (distanceSourceToDestination == -1) {
            //check isExistsRoute
        }
        float price = this.getPrice(distanceSourceToDestination, source, destination);

        transactionRepository.save(wrapperTransactionEntity(fareCalculatorRequest, price));
        return wrapperCalculatedFareResponse(fareCalculatorRequest, distanceSourceToDestination, price);
    }

    private void validateFareCalculatorRequest(FareCalculatorRequest fareCalculatorRequest) {
        //check source exists
        //check Destination exists
        //check station deprecated?
        //check type

    }

    private float getPrice(int distanceSourceToDestination, String source, String destination) {
        FareRateEntity fareRateEntity;
        if (!Objects.equals(source, destination)) {
            fareRateEntity = fareRateRepository.findTopByOrderByDistanceDesc();
            fareRateEntity = (distanceSourceToDestination >= fareRateEntity.getDistance())
                    ? fareRateEntity
                    : fareRateRepository.findOneByDistance(distanceSourceToDestination);
        } else {
            fareRateEntity = fareRateRepository.findTopByOrderByDistanceAsc();
        }
        ExtendMappingEntity sourceExtendMappingEntity = extendMappingRepository.findByStationName(source);
        ExtendMappingEntity destinationExtendMappingEntity = extendMappingRepository.findByStationName(destination);

        return getFareRateEntityPrice(fareRateEntity, sourceExtendMappingEntity, destinationExtendMappingEntity);
    }

    private static float getFareRateEntityPrice(FareRateEntity fareRateEntity, ExtendMappingEntity sourceExtendMappingEntity, ExtendMappingEntity destinationExtendMappingEntity) {
        float fareRateEntityPrice = fareRateEntity.getPrice();
        float sourceExtendPrice = sourceExtendMappingEntity != null ? sourceExtendMappingEntity.getExtendPriceEntity().getExtendPrice() : 0;
        float destinationExtendPrice = destinationExtendMappingEntity != null ? destinationExtendMappingEntity.getExtendPriceEntity().getExtendPrice() : 0;

        if (sourceExtendMappingEntity != null && destinationExtendMappingEntity != null) {
            if (sourceExtendMappingEntity.getExtendPriceEntity().getId() == destinationExtendMappingEntity.getExtendPriceEntity().getId()) {
                fareRateEntityPrice = sourceExtendPrice;
            } else {
                fareRateEntityPrice += sourceExtendPrice;
            }
        } else if (sourceExtendMappingEntity != null) {
            fareRateEntityPrice += sourceExtendPrice;
        } else if (destinationExtendMappingEntity != null) {
            fareRateEntityPrice += destinationExtendPrice;
        }
        return fareRateEntityPrice;
    }

    private TransactionEntity wrapperTransactionEntity(FareCalculatorRequest fareCalculatorRequest, float price) {
        return TransactionEntity.formTransactionEntity(fareCalculatorRequest, price);
    }

    private CalculatedFareResponse wrapperCalculatedFareResponse(FareCalculatorRequest fareCalculatorRequest, int distance, float price) {
        CalculatedFareResponse response = new CalculatedFareResponse();
        response.setSource(fareCalculatorRequest.getSource());
        response.setDestination(fareCalculatorRequest.getDestination());
        response.setDistance(distance);
        response.setPrice(price);
        return response;
    }
}
