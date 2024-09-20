package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.ExtendMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendMappingRepository extends JpaRepository<ExtendMappingEntity, Integer> {

    ExtendMappingEntity findByStationName(String stationName);
}