package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.FareRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FareRateRepository extends JpaRepository<FareRateEntity, Integer> {
    List<FareRateEntity> findAllByTrainColorOrderByIdAsc(String trainColor);
    FareRateEntity findOneByTrainColorAndDistance(String trainColor, float distance);
}
