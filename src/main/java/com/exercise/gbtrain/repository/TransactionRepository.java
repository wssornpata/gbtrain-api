package com.exercise.gbtrain.repository;

import com.exercise.gbtrain.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    List<TransactionEntity> findAllByOrderByIdAsc();

}
