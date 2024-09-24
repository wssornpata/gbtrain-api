package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.dto.farecalculator.response.CalculatedFareResponse;
import com.exercise.gbtrain.dto.farecalculator.response.FareRateResponse;
import com.exercise.gbtrain.entity.ExtendMappingEntity;
import com.exercise.gbtrain.entity.ExtendPriceEntity;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.entity.TransactionEntity;
import com.exercise.gbtrain.exception.EntityNotFoundException;
import com.exercise.gbtrain.exception.GlobalRuntimeException;
import com.exercise.gbtrain.exception.RouteNotFoundException;
import com.exercise.gbtrain.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FareCalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);

    private final FareRateRepository fareRateRepository;
    private final TransactionRepository transactionRepository;
    private final ExtendMappingRepository extendMappingRepository;
    private final ExtendPriceRepository extendPriceRepository;
    private final GraphService graphService;
    private final StationRepository stationRepository;
    private final TypeRepository typeRepository;

    public FareCalculatorService(FareRateRepository fareRateRepository,
                                 TransactionRepository transactionRepository,
                                 ExtendMappingRepository extendMappingRepository,
                                 ExtendPriceRepository extendPriceRepository,
                                 GraphService graphService, StationRepository stationRepository, TypeRepository typeRepository) {
        this.fareRateRepository = fareRateRepository;
        this.transactionRepository = transactionRepository;
        this.extendMappingRepository = extendMappingRepository;
        this.extendPriceRepository = extendPriceRepository;
        this.graphService = graphService;
        this.stationRepository = stationRepository;
        this.typeRepository = typeRepository;
    }

    public CalculatedFareResponse calculateFare(FareCalculatorRequest request) {
            validateFareCalculatorRequest(request);

            String source = request.getSource();
            String destination = request.getDestination();
            int type = request.getType();
            int distance = graphService.getDistance(source, destination, type);

            if (!hasRoute(distance)) {
                throw new RouteNotFoundException("No route found between source and destination");
            }

            float price = calculatePrice(distance, source, destination, type);
            transactionRepository.save(createTransactionEntity(request, price));

            return wrapperCalculatedFareResponse(request, distance, price, type);
    }

    public void validateFareCalculatorRequest(FareCalculatorRequest request) {
        if (!stationRepository.existsByStationName(request.getSource())) {
            throw new EntityNotFoundException("Source does not exists");
        }

        if (!stationRepository.existsByStationName(request.getDestination())) {
            throw new EntityNotFoundException("Destination does not exists");
        }

        if (!typeRepository.existsByType(request.getType())) {
            throw new EntityNotFoundException("Type does not exists");
        }
    }

    private boolean hasRoute(int distance) {
        return distance != -1;
    }

    private float calculatePrice(int distance, String source, String destination, int type) {
        FareRateEntity fareRateEntity = getFareRateEntity(distance, source, destination);

        if (fareRateEntity == null) {
            throw new EntityNotFoundException("Fare rate does not exists");
        }

        if (type == 2) {
            ExtendMappingEntity sourceMapping = extendMappingRepository.findByStationName(source);
            ExtendMappingEntity destinationMapping = extendMappingRepository.findByStationName(destination);
            return calculateExtendPrice(fareRateEntity, sourceMapping, destinationMapping);
        } else {
            return fareRateEntity.getPrice();
        }
    }

    private FareRateEntity getFareRateEntity(int distance, String source, String destination) {
        if (Objects.equals(source, destination)) {
            return fareRateRepository.findTopByOrderByDistanceAsc();
        } else {
            FareRateEntity maxDistanceEntity = fareRateRepository.findTopByOrderByDistanceDesc();
            return (distance >= maxDistanceEntity.getDistance())
                    ? maxDistanceEntity
                    : fareRateRepository.findOneByDistance(distance);
        }
    }

    private float calculateExtendPrice(FareRateEntity fareRateEntity,
                                       ExtendMappingEntity sourceMapping,
                                       ExtendMappingEntity destinationMapping) {

        float basePrice = fareRateEntity.getPrice();

        boolean isSourceExtend = isExtend(sourceMapping);
        boolean isDestinationExtend = isExtend(destinationMapping);

        if (isSourceExtend && isDestinationExtend) {
            return calculatePriceForBothExtends(basePrice, sourceMapping, destinationMapping);
        } else if (isSourceExtend) {
            return basePrice + sourceMapping.getExtendPriceEntity().getExtendPrice();
        } else if (isDestinationExtend) {
            return basePrice + destinationMapping.getExtendPriceEntity().getExtendPrice();
        }

        return basePrice;
    }

    private float calculatePriceForBothExtends(float basePrice,
                                               ExtendMappingEntity sourceMapping,
                                               ExtendMappingEntity destinationMapping) {
        if (isSameExtendPriceEntity(sourceMapping, destinationMapping)) {
            return sourceMapping.getExtendPriceEntity().getExtendPrice();
        } else {
            String combinedExtendName = sourceMapping.getExtendPriceEntity().getExtendName() +
                    destinationMapping.getExtendPriceEntity().getExtendName();
            ExtendPriceEntity additionalPriceEntity = extendPriceRepository.findByExtendName(combinedExtendName);
            if (additionalPriceEntity != null) {
                return basePrice + additionalPriceEntity.getExtendPrice();
            }
            return basePrice + sourceMapping.getExtendPriceEntity().getExtendPrice();
        }
    }

    private boolean isExtend(ExtendMappingEntity mapping) {
        return mapping != null;
    }

    private boolean isSameExtendPriceEntity(ExtendMappingEntity sourceMapping,
                                            ExtendMappingEntity destinationMapping) {
        return sourceMapping.getExtendPriceEntity().getId() == destinationMapping.getExtendPriceEntity().getId();
    }

    private TransactionEntity createTransactionEntity(FareCalculatorRequest request, float price) {
        return TransactionEntity.formTransactionEntity(request, price);
    }

    private CalculatedFareResponse wrapperCalculatedFareResponse(FareCalculatorRequest request, int distance, float price, int type) {
        return new CalculatedFareResponse(
                request.getSource(),
                request.getDestination(),
                distance,
                price,
                type
        );
    }
}