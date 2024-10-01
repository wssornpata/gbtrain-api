package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.ExtendPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtendPriceRepository extends JpaRepository<ExtendPriceEntity , Integer > {

    ExtendPriceEntity findByExtendName(String extendName);
}
