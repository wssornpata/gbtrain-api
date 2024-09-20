package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.FareRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FareRateRepository extends JpaRepository<FareRateEntity, Integer> {

    List<FareRateEntity> findAllByOrderByIdAsc();
    FareRateEntity findOneByDistance(int distance);
    FareRateEntity findTopByOrderByDistanceDesc();
    FareRateEntity findTopByOrderByDistanceAsc();
    Boolean existsByDistance(int distance);
}
