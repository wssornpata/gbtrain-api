package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.exception.InvalidEntityAndTypoException;
import com.exercise.gbtrain.repository.FareRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        validatePriceAdjustorRequestList(priceAdjustorRequestList);

        Map<Integer, PriceAdjustorRequest> requestMap = priceAdjustorRequestList.stream()
                .collect(Collectors.toMap(PriceAdjustorRequest::getId, request -> request));

        List<Integer> priceAdjusterRequestIdList = new ArrayList<>(requestMap.keySet());
        List<FareRateEntity> fareRateEntities = fareRateRepository.findAllById(priceAdjusterRequestIdList);
        LocalDateTime now = LocalDateTime.now();

        for (FareRateEntity fareRateEntity : fareRateEntities) {
            PriceAdjustorRequest priceAdjustorRequest = requestMap.get(fareRateEntity.getId());
            if (priceAdjustorRequest != null) {
                validateAdjustRequest(priceAdjustorRequest);

                fareRateEntity.setDescription(priceAdjustorRequest.getDescription());
                fareRateEntity.setPrice(priceAdjustorRequest.getPrice());
                fareRateEntity.setUpdateDatetime(now);
            }
        }
        return fareRateRepository.saveAll(fareRateEntities);
    }

    private void validatePriceAdjustorRequestList(List<PriceAdjustorRequest> priceAdjustorRequestList) {
        if (priceAdjustorRequestList == null || priceAdjustorRequestList.isEmpty()) {
            throw new InvalidEntityAndTypoException("Request list cannot be null or empty", "Invalid Request");
        }
    }

    private void validateAdjustRequest(PriceAdjustorRequest request) {
        if (request == null) {
            throw new InvalidEntityAndTypoException("Request cannot be null", "Invalid Request");
        }

        if (request.getId() <= 0) {
            throw new InvalidEntityAndTypoException("ID must be greater than 0", "Invalid ID");
        }

        if (request.getDescription() == null || request.getDescription().isEmpty() || request.getDescription().length() > 255) {
            throw new InvalidEntityAndTypoException("Description cannot be null or empty", "Invalid Description");
        }

        if (request.getDistance() < 0) {
            throw new InvalidEntityAndTypoException("Distance cannot be negative", "Invalid Distance");
        }

        if (request.getPrice() < 0) {
            throw new InvalidEntityAndTypoException("Price cannot be negative", "Invalid Price");
        }
    }
}
