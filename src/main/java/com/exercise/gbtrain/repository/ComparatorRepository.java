package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.ComparatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComparatorRepository extends JpaRepository<ComparatorEntity, Integer> {

    ComparatorEntity findOneBySourceAndDestination(String source, String destination);
}
