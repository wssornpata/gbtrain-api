package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationEntity, String> {

    boolean existsByStationName(String stationName);
    StationEntity findByStationName(String stationName);
    List<StationEntity> findAllByOrderByIdAsc();
}
