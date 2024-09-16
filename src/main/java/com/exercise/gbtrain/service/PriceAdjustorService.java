package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorDetailRequest;
import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.dto.priceadjustor.response.PriceAdjustorResponse;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.repository.FareRateRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;


@Service
public class PriceAdjustorService {
    private final Logger logger = LoggerFactory.getLogger(PriceAdjustorService.class);

    private final FareRateRepository fareRateRepository;

    public PriceAdjustorService(FareRateRepository fareRateRepository) {
        this.fareRateRepository = fareRateRepository;
    }

    public List<PriceAdjustorResponse> getPrice() {
        List<FareRateEntity> fareRateEntityList = fareRateRepository.findAll();
        return wrapperPriceAdjustorResponse(fareRateEntityList);
    }

    public List<PriceAdjustorResponse> getPrice(String trainColor) {
        List<FareRateEntity> fareRateEntityList = fareRateRepository.findAllByTrainColorOrderByIdAsc(trainColor);
        return wrapperPriceAdjustorResponse(fareRateEntityList);
    }

    private List<PriceAdjustorResponse> wrapperPriceAdjustorResponse(List<FareRateEntity> fareRateEntityList) {
        List<PriceAdjustorResponse> responseList = new ArrayList<>();

        return responseList;
    }

    @Transactional
    public List<PriceAdjustorResponse> adjustPrice(PriceAdjustorRequest priceAdjustorRequest) {
        String trainColor = priceAdjustorRequest.getTrainColor();
        LocalDateTime now = LocalDateTime.now();
        for (PriceAdjustorDetailRequest priceAdjustorDetailRequest : priceAdjustorRequest.getPriceAdjustorDetailRequests()) {
            fareRateRepository.findById(priceAdjustorDetailRequest.getId())
                    .ifPresent(fareRateEntity -> {
                        fareRateEntity.setPrice(priceAdjustorDetailRequest.getPrice());
                        fareRateEntity.setDistance(priceAdjustorDetailRequest.getDistance());
                        fareRateEntity.setUpdateDatetime(now);
                        fareRateRepository.save(fareRateEntity);
                    });
        }

        return this.getPrice(trainColor);
    }
}
