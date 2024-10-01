package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import com.exercise.gbtrain.dto.farecalculator.response.CalculatedFareResponse;
import com.exercise.gbtrain.entity.ExtendMappingEntity;
import com.exercise.gbtrain.entity.ExtendPriceEntity;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.entity.TransactionEntity;
import com.exercise.gbtrain.exception.InvalidEntityAndTypoException;
import com.exercise.gbtrain.exception.RouteNotFoundException;
import com.exercise.gbtrain.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.exercise.gbtrain.dto.farecalculator.response.CalculatedFareResponse.wrapperCalculatedFareResponse;

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

        ExtendMappingEntity sourceMapping = extendMappingRepository.findByStationName(source);
        ExtendMappingEntity destinationMapping = extendMappingRepository.findByStationName(destination);
        int distance = graphService.getDistance(source, destination, type, sourceMapping, destinationMapping);

        validateRouteExists(distance);

        float price = calculatePrice(distance, source, destination, type, sourceMapping, destinationMapping);

        transactionRepository.save(createTransactionEntity(request, price));
        return wrapperCalculatedFareResponse(request, price, type);
    }

    public void validateFareCalculatorRequest(FareCalculatorRequest request) {
        if (request == null) {
            throw new InvalidEntityAndTypoException("Request cannot be null", "Invalid Request");
        }

        if (request.getSource() == null || request.getSource().isEmpty()) {
            throw new InvalidEntityAndTypoException("Source cannot be null or empty", "Invalid Source");
        }

        if (request.getDestination() == null || request.getDestination().isEmpty()) {
            throw new InvalidEntityAndTypoException("Destination cannot be null or empty", "Invalid Destination");
        }

        if (request.getType() == null || request.getType() < 0) {
            throw new InvalidEntityAndTypoException("Type cannot be null or negative", "Invalid Type");
        }

        if (!stationRepository.existsByStationName(request.getSource())) {
            throw new InvalidEntityAndTypoException("Source does not exist", "Invalid Source");
        }

        if (!stationRepository.existsByStationName(request.getDestination())) {
            throw new InvalidEntityAndTypoException("Destination does not exist", "Invalid Destination");
        }

        if (!typeRepository.existsByType(request.getType())) {
            throw new InvalidEntityAndTypoException("Type does not exist", "Invalid Type");
        }
    }

    public void validateRouteExists(int distance) {
        if (!hasRoute(distance)) {
            throw new RouteNotFoundException("No route found between source and destination");
        }
    }

    private boolean hasRoute(int distance) {
        return distance != -1;
    }

    float calculatePrice(int distance, String source, String destination, int type, ExtendMappingEntity sourceMapping, ExtendMappingEntity destinationMapping) {
        FareRateEntity fareRateEntity = getFareRateEntity(distance);

        validateFareRateEntity(fareRateEntity);

        if (type == 2) {
            return calculateExtendPrice(fareRateEntity, source, destination, sourceMapping, destinationMapping);
        }
        return fareRateEntity.getPrice();
    }

    private void validateFareRateEntity(FareRateEntity fareRateEntity) {
        if (fareRateEntity == null) {
            throw new InvalidEntityAndTypoException("Source does not exist", "Invalid Source");
        }
    }

    private FareRateEntity getFareRateEntity(int distance) {
        return fareRateRepository.findOneMatchFareRateWithDistance(distance);
    }

    private float calculateExtendPrice(FareRateEntity fareRateEntity,
                                       String source,
                                       String destination,
                                       ExtendMappingEntity sourceMapping,
                                       ExtendMappingEntity destinationMapping) {

        float basePrice = fareRateEntity.getPrice();

        boolean isSourceExtend = isExtend(sourceMapping);
        boolean isDestinationExtend = isExtend(destinationMapping);

        if (isSourceExtend && isDestinationExtend) {
            return calculatePriceForBothExtends(basePrice, sourceMapping, destinationMapping);
        } else if (isSourceExtend) {
            return calculatePriceForStartExtendsStation(source, destination, sourceMapping, null, basePrice, sourceMapping.getExtendPriceEntity());
        } else if (isDestinationExtend) {
            return calculatePriceForStartExtendsStation(source, destination, null, destinationMapping, basePrice, destinationMapping.getExtendPriceEntity());
        }
        return basePrice;
    }

    private float calculatePriceForStartExtendsStation(String source, String destination, ExtendMappingEntity sourceMapping, ExtendMappingEntity destinationMapping, float basePrice, ExtendPriceEntity extendPriceEntity) {
        if (source.equals("N8") && destinationMapping.getExtendPriceEntity().getExtendName().equals("Sukhumvit2")) {
            return destinationMapping.getExtendPriceEntity().getExtendPrice();
        }
        if (destination.equals("N8") && sourceMapping.getExtendPriceEntity().getExtendName().equals("Sukhumvit2")) {
            return sourceMapping.getExtendPriceEntity().getExtendPrice();
        }
        if (source.equals("E9") && destinationMapping.getExtendPriceEntity().getExtendName().equals("Sukhumvit1")) {
            return destinationMapping.getExtendPriceEntity().getExtendPrice();
        }
        if (destination.equals("E9") && sourceMapping.getExtendPriceEntity().getExtendName().equals("Sukhumvit1")) {
            return sourceMapping.getExtendPriceEntity().getExtendPrice();
        }
        return basePrice + extendPriceEntity.getExtendPrice();
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
        }
        return basePrice + sourceMapping.getExtendPriceEntity().getExtendPrice();
    }

    private boolean isExtend(ExtendMappingEntity mapping) {
        return mapping != null;
    }

    boolean isSameExtendPriceEntity(ExtendMappingEntity sourceMapping,
                                    ExtendMappingEntity destinationMapping) {
        return sourceMapping.getExtendPriceEntity().getId() == destinationMapping.getExtendPriceEntity().getId();
    }

    private TransactionEntity createTransactionEntity(FareCalculatorRequest request, float price) {
        return TransactionEntity.formTransactionEntity(request, price);
    }

}