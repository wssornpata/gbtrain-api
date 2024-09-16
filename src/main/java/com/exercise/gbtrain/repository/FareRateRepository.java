package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.ColorMappingEntity;
import com.exercise.gbtrain.entity.FareRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FareRateRepository extends JpaRepository<FareRateEntity, Integer> {

    List<FareRateEntity> findAllByTrainColorOrderByIdAsc(String trainColor);
}
