package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.StationEntity;
import com.exercise.gbtrain.entity.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Integer> {

}
