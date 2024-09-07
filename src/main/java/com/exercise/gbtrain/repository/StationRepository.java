package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<StationEntity, String> {

}
