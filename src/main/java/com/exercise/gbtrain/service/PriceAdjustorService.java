package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.entity.FareRateEntity;
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
        Map<Integer, PriceAdjustorRequest> requestMap = priceAdjustorRequestList.stream()
                .collect(Collectors.toMap(PriceAdjustorRequest::getId, request -> request));

        List<Integer> priceAdjusterRequestIdList = new ArrayList<>(requestMap.keySet());
        List<FareRateEntity> fareRateEntities = fareRateRepository.findAllById(priceAdjusterRequestIdList);
        LocalDateTime now = LocalDateTime.now();

        for (FareRateEntity fareRateEntity : fareRateEntities) {
            PriceAdjustorRequest priceAdjustorRequest = requestMap.get(fareRateEntity.getId());
            if (priceAdjustorRequest != null) {

                fareRateEntity.setDescription(priceAdjustorRequest.getDescription());
                fareRateEntity.setPrice(priceAdjustorRequest.getPrice());
                fareRateEntity.setUpdateDatetime(now);
            }
        }
        return fareRateRepository.saveAll(fareRateEntities);
    }

    private void validateFareRateEntity(FareRateEntity fareRateEntity) {

    }
}
