package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.priceadjustor.request.GetPriceAdjustorRequest;
import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorDetailRequest;
import com.exercise.gbtrain.dto.priceadjustor.request.PriceAdjustorRequest;
import com.exercise.gbtrain.entity.FareRateEntity;
import com.exercise.gbtrain.repository.FareRateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PriceAdjustorService {
    private final Logger logger = LoggerFactory.getLogger(PriceAdjustorService.class);

    private final FareRateRepository fareRateRepository;

    public PriceAdjustorService(FareRateRepository fareRateRepository) {
        this.fareRateRepository = fareRateRepository;
    }

    public List<FareRateEntity> getPrice(GetPriceAdjustorRequest getPriceAdjustorRequest) {
        return fareRateRepository.findAllByTrainColorOrderByIdAsc(getPriceAdjustorRequest.getTrainColor());
    }

    public List<FareRateEntity> getPrice(PriceAdjustorRequest priceAdjustorRequest) {
        return fareRateRepository.findAllByTrainColorOrderByIdAsc(priceAdjustorRequest.getTrainColor());
    }

    @Transactional
    public String createPrice(PriceAdjustorRequest priceAdjustorRequest) {
        validateRequest(priceAdjustorRequest);

        String trainColor = priceAdjustorRequest.getTrainColor();
        LocalDateTime now = LocalDateTime.now();

        for (PriceAdjustorDetailRequest detailRequest : priceAdjustorRequest.getPriceAdjustorDetailRequests()) {
            createFareRate(trainColor, detailRequest, now);
        }

        return trainColor;
    }

    private void validateRequest(PriceAdjustorRequest request) {
    }

    private void createFareRate(String trainColor, PriceAdjustorDetailRequest detailRequest, LocalDateTime now) {
        if(fareRateRepository.existsByTrainColorAndDistance(trainColor, detailRequest.getDistance())){
            throw new EntityNotFoundException("Price adjustor with train color " + trainColor + " already exists");
        }
        fareRateRepository.save(wrapperCreateFareRateEntity(trainColor, detailRequest, now));
    }

    private FareRateEntity wrapperCreateFareRateEntity(String trainColor, PriceAdjustorDetailRequest detailRequest, LocalDateTime now) {
        FareRateEntity fareRateEntity = new FareRateEntity();
        fareRateEntity.setTrainColor(trainColor);
        fareRateEntity.setDescription(detailRequest.getDescription());
        fareRateEntity.setDistance(detailRequest.getDistance());
        fareRateEntity.setPrice(detailRequest.getPrice());
        fareRateEntity.setUpdateDatetime(now);
        return fareRateEntity;
    }

    @Transactional
    public String adjustPrice(PriceAdjustorRequest priceAdjustorRequest) {
        validateRequest(priceAdjustorRequest);

        String trainColor = priceAdjustorRequest.getTrainColor();
        LocalDateTime now = LocalDateTime.now();

        for (PriceAdjustorDetailRequest detailRequest : priceAdjustorRequest.getPriceAdjustorDetailRequests()) {
            updateFareRate(trainColor, detailRequest, now);
        }

        return trainColor;
    }

    private void updateFareRate(String trainColor, PriceAdjustorDetailRequest detailRequest, LocalDateTime now) {
        FareRateEntity fareRateEntity = fareRateRepository.findOneByTrainColorAndDistance(
                trainColor,
                detailRequest.getDistance());

        if (fareRateEntity == null) {
            throw new EntityNotFoundException("Fare rate not found");
        }

        fareRateRepository.save(wrapperUpdateFareRate(detailRequest, now));
    }

    private FareRateEntity wrapperUpdateFareRate(PriceAdjustorDetailRequest detailRequest, LocalDateTime now) {
        FareRateEntity fareRateEntity = new FareRateEntity();
        fareRateEntity.setDescription(detailRequest.getDescription());
        fareRateEntity.setPrice(detailRequest.getPrice());
        fareRateEntity.setUpdateDatetime(now);
        return fareRateEntity;
    }
}
