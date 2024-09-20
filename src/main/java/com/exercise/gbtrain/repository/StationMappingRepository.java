package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.StationMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationMappingRepository extends JpaRepository<StationMappingEntity, Integer> {

}