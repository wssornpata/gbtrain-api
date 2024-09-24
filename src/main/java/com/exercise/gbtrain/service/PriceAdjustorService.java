package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.exception.EntityNotFoundException;
import com.exercise.gbtrain.exception.GlobalRuntimeException;
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
