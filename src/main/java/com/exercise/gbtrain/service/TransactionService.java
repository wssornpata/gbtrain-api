package com.exercise.gbtrain.service;

import com.exercise.gbtrain.entity.TransactionEntity;
import com.exercise.gbtrain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionEntity> getTransactions() {
        return transactionRepository.findAllByOrderByIdDesc();
    }
}
