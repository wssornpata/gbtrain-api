package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.exception.EntityNotFoundException;
import com.exercise.gbtrain.exception.GlobalRuntimeException;
import com.exercise.gbtrain.exception.TypoException;
import com.exercise.gbtrain.repository.FareRateRepository;
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

    @Transactional(readOnly = true)
    public List<FareRateEntity> getfarerate() {
        return fareRateRepository.findAllByOrderByIdAsc();
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
        if (request == null) {
            throw new TypoException("Request cannot be null");
        }

        if (request.getId() <= 0) {
            throw new TypoException("ID must be greater than 0");
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new TypoException("Description cannot be null or empty");
        }
        if (request.getDistance() < 0) {
            throw new TypoException("Distance cannot be negative");
        }
        if (request.getPrice() < 0) {
            throw new TypoException("Price cannot be negative");
        }
    }

    private void updateFareRate(PriceAdjustorRequest priceAdjustorRequest, LocalDateTime now) {
        FareRateEntity fareRateEntity = fareRateRepository.findOneByDistance(priceAdjustorRequest.getDistance());
        if (fareRateEntity == null) {
            throw new EntityNotFoundException("Fare rate does not exist");
        }
        fareRateEntity.setDescription(priceAdjustorRequest.getDescription());
        fareRateEntity.setPrice(priceAdjustorRequest.getPrice());
        fareRateEntity.setUpdateDatetime(now);
        fareRateRepository.save(fareRateEntity);
    }
}
