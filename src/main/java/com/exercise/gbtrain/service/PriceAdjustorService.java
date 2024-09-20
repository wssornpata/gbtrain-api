package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.repository.FareRateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PriceAdjustorService {
    private final Logger logger = LoggerFactory.getLogger(PriceAdjustorService.class);

    private final FareRateRepository fareRateRepository;

    public PriceAdjustorService(FareRateRepository fareRateRepository) {
        this.fareRateRepository = fareRateRepository;
    }

    public List<FareRateEntity> getPrice() {
        return fareRateRepository.findAllByOrderByIdAsc();
    }

    @Transactional
    public List<FareRateEntity> createPrice(List<PriceAdjustorRequest> priceAdjustorRequestList) {
        LocalDateTime now = LocalDateTime.now();

        for (PriceAdjustorRequest priceAdjustorRequest : priceAdjustorRequestList) {
            validateCreateRequest(priceAdjustorRequest);
            createFareRate(priceAdjustorRequest, now);
        }

        return fareRateRepository.findAllByOrderByIdAsc();
    }

    private void validateCreateRequest(PriceAdjustorRequest priceAdjustorRequest) {

    }

    private void createFareRate(PriceAdjustorRequest priceAdjustorRequest, LocalDateTime now) {
        if(fareRateRepository.existsByDistance(priceAdjustorRequest.getDistance())){
            throw new EntityNotFoundException("Distance "+ priceAdjustorRequest.getDistance() +" price is already exists");
        }
        fareRateRepository.save(wrapperCreateFareRateEntity(priceAdjustorRequest, now));
    }

    private FareRateEntity wrapperCreateFareRateEntity(PriceAdjustorRequest priceAdjustorRequest, LocalDateTime now) {
        FareRateEntity fareRateEntity = new FareRateEntity();
        fareRateEntity.setDescription(priceAdjustorRequest.getDescription());
        fareRateEntity.setDistance(priceAdjustorRequest.getDistance());
        fareRateEntity.setPrice(priceAdjustorRequest.getPrice());
        fareRateEntity.setUpdateDatetime(now);
        return fareRateEntity;
    }

    @Transactional
    public List<FareRateEntity> adjustPrice(List<PriceAdjustorRequest> priceAdjustorRequestList) {
        LocalDateTime now = LocalDateTime.now();
        for (PriceAdjustorRequest priceAdjustorRequest : priceAdjustorRequestList) {
            validateAdjustRequest(priceAdjustorRequest);
            updateFareRate(priceAdjustorRequest, now);
        }
        return fareRateRepository.findAllByOrderByIdAsc();
    }

    private void validateAdjustRequest(PriceAdjustorRequest request) {
    }

    private void updateFareRate(PriceAdjustorRequest priceAdjustorRequest, LocalDateTime now) {
        FareRateEntity fareRateEntity = fareRateRepository.findOneByDistance(priceAdjustorRequest.getDistance());
        if (fareRateEntity == null) {
            throw new EntityNotFoundException("Fare rate not found");
        }
        fareRateRepository.save(wrapperUpdateFareRateEntity(priceAdjustorRequest, now));
    }

    private FareRateEntity wrapperUpdateFareRateEntity(PriceAdjustorRequest priceAdjustorRequest, LocalDateTime now) {
        FareRateEntity fareRateEntity = new FareRateEntity();
        fareRateEntity.setDescription(priceAdjustorRequest.getDescription());
        fareRateEntity.setPrice(priceAdjustorRequest.getPrice());
        fareRateEntity.setUpdateDatetime(now);
        return fareRateEntity;
    }
}
