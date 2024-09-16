package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.ColorMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorMappingRepository extends JpaRepository<ColorMappingEntity, String> {

}
