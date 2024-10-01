package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.FareRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FareRateRepository extends JpaRepository<FareRateEntity, Integer> {

    List<FareRateEntity> findAllByOrderByIdAsc();

    @Query(value = "SELECT " +
            "f.farerate_distance, " +
            "f.farerate_price, " +
            "f.farerate_id, " +
            "f.farerate_description, " +
            "f.update_datetime " +
            "FROM " +
            "gbtrain.farerate f " +
            "WHERE " +
            "f.farerate_distance <= :distance " +
            "ORDER BY " +
            "f.farerate_price DESC " +
            "LIMIT 1"
            , nativeQuery = true)
    FareRateEntity findOneMatchFareRateWithDistance(@Param("distance") int distance);
}
