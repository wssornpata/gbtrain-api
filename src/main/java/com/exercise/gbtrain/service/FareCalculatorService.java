package com.exercise.gbtrain.service;

import com.exercise.gbtrain.configuration.ExtendConfig;
import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.dto.farecalculator.response.CalculatedFareResponse;
import com.exercise.gbtrain.entity.*;
import com.exercise.gbtrain.exception.InvalidEntityAndTypoException;
import com.exercise.gbtrain.repository.*;
import org.springframework.stereotype.Service;

import static com.exercise.gbtrain.dto.farecalculator.response.CalculatedFareResponse.wrapperCalculatedFareResponse;

@Service
public class FareCalculatorService {

    private final ExtendConfig extendConfig;

    private final FareRateRepository fareRateRepository;
    private final TransactionRepository transactionRepository;
    private final ExtendMappingRepository extendMappingRepository;
    private final ExtendPriceRepository extendPriceRepository;
    private final StationRepository stationRepository;
    private final TypeRepository typeRepository;

    private final GraphService graphService;

    public FareCalculatorService(ExtendConfig extendConfig, FareRateRepository fareRateRepository,
                                 TransactionRepository transactionRepository,
                                 ExtendMappingRepository extendMappingRepository,
                                 ExtendPriceRepository extendPriceRepository,
                                 GraphService graphService, StationRepository stationRepository, TypeRepository typeRepository) {
        this.extendConfig = extendConfig;
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

        String origin = request.getOrigin();
        String destination = request.getDestination();
        int type = request.getType();

        ExtendMappingEntity originMapping = extendMappingRepository.findByStationName(origin);
        ExtendMappingEntity destinationMapping = extendMappingRepository.findByStationName(destination);
        int distance = graphService.getDistance(origin, destination, type, originMapping, destinationMapping);
//
//        validateRouteExists(distance);

        float price = calculatePrice(distance, origin, destination, type, originMapping, destinationMapping);


        transactionRepository.save(createTransactionEntity(request, price));

        StationEntity originStationEntity = stationRepository.findByStationName(origin);
        StationEntity destinationStationEntity = stationRepository.findByStationName(destination);
        TypeEntity typeEntity = typeRepository.findByType(type);
        return wrapperCalculatedFareResponse(originStationEntity, destinationStationEntity, price, typeEntity.getDescription());
    }

    public void validateFareCalculatorRequest(FareCalculatorRequest request) {
        if (!stationRepository.existsByStationName(request.getOrigin())) {
            throw new InvalidEntityAndTypoException( "Invalid Origin", "Origin does not exist");
        }

        if (!stationRepository.existsByStationName(request.getDestination())) {
            throw new InvalidEntityAndTypoException( "Invalid Destination", "Destination does not exist");
        }

        if (!typeRepository.existsByType(request.getType())) {
            throw new InvalidEntityAndTypoException("Invalid Type", "Type does not exist");
        }
    }

    float calculatePrice(int distance, String origin, String destination, int type, ExtendMappingEntity originMapping, ExtendMappingEntity destinationMapping) {
        FareRateEntity fareRateEntity = getFareRateEntity(distance);
        validateFareRateEntity(fareRateEntity);

        if (type == 2) {
            return calculateExtendPrice(fareRateEntity, origin, destination, originMapping, destinationMapping);
        }
        return fareRateEntity.getPrice();
    }

    private void validateFareRateEntity(FareRateEntity fareRateEntity) {
        if (fareRateEntity == null) {
            throw new InvalidEntityAndTypoException("Farerate not found", "Invalid Farerate");
        }
    }

    private FareRateEntity getFareRateEntity(int distance) {
        return fareRateRepository.findOneMatchFareRateWithDistance(distance);
    }

    private float calculateExtendPrice(FareRateEntity fareRateEntity,
                                       String origin,
                                       String destination,
                                       ExtendMappingEntity originMapping,
                                       ExtendMappingEntity destinationMapping) {

        float basePrice = fareRateEntity.getPrice();

        boolean isOriginExtend = isExtend(originMapping);
        boolean isDestinationExtend = isExtend(destinationMapping);

        if (isOriginExtend && isDestinationExtend) {
            return calculatePriceForBothExtends(basePrice, originMapping, destinationMapping);
        } else if (isOriginExtend) {
            return calculatePriceForSingleExtend(origin, destination, originMapping, null, basePrice);
        } else if (isDestinationExtend) {
            return calculatePriceForSingleExtend(origin, destination, null, destinationMapping, basePrice);
        }
        return basePrice;
    }

    private float calculatePriceForSingleExtend(String origin, String destination,
                                                ExtendMappingEntity originMapping,
                                                ExtendMappingEntity destinationMapping,
                                                float basePrice) {
        if (isMatchingExtend(origin, destinationMapping, extendConfig.getStartStationA(), extendConfig.getNameStationA()) ||
            isMatchingExtend(origin, destinationMapping, extendConfig.getStartStationB(), extendConfig.getNameStationB())) {
            return destinationMapping.getExtendPriceEntity().getExtendPrice();
        }
        if (isMatchingExtend(destination, originMapping, extendConfig.getStartStationA(), extendConfig.getNameStationA()) ||
            isMatchingExtend(destination, originMapping, extendConfig.getStartStationB(), extendConfig.getNameStationB())) {
            return originMapping.getExtendPriceEntity().getExtendPrice();
        }
        return basePrice + getExtendPrice(originMapping, destinationMapping);
    }

    private boolean isMatchingExtend(String station, ExtendMappingEntity mapping, String startStation, String nameStation) {
        return station.equals(startStation) && mapping != null &&
                mapping.getExtendPriceEntity().getExtendName().equals(nameStation);
    }

    private float getExtendPrice(ExtendMappingEntity originMapping, ExtendMappingEntity destinationMapping) {
        if (originMapping != null) {
            return originMapping.getExtendPriceEntity().getExtendPrice();
        }
        if (destinationMapping != null) {
            return destinationMapping.getExtendPriceEntity().getExtendPrice();
        }
        return 0;
    }
    private float calculatePriceForBothExtends(float basePrice,
                                               ExtendMappingEntity originMapping,
                                               ExtendMappingEntity destinationMapping) {
        if (isSameExtendPriceEntity(originMapping, destinationMapping)) {
            return originMapping.getExtendPriceEntity().getExtendPrice();
        } else {
            String combinedExtendName = originMapping.getExtendPriceEntity().getExtendName() +
                    destinationMapping.getExtendPriceEntity().getExtendName();
            ExtendPriceEntity additionalPriceEntity = extendPriceRepository.findByExtendName(combinedExtendName);
            if (additionalPriceEntity != null) {
                return basePrice + additionalPriceEntity.getExtendPrice();
            }
        }
        return basePrice + originMapping.getExtendPriceEntity().getExtendPrice();
    }

    private boolean isExtend(ExtendMappingEntity mapping) {
        return mapping != null;
    }

    boolean isSameExtendPriceEntity(ExtendMappingEntity originMapping,
                                    ExtendMappingEntity destinationMapping) {
        return originMapping.getExtendPriceEntity().getId() == destinationMapping.getExtendPriceEntity().getId();
    }

    private TransactionEntity createTransactionEntity(FareCalculatorRequest request, float price) {
        return TransactionEntity.formTransactionEntity(request, price);
    }

}