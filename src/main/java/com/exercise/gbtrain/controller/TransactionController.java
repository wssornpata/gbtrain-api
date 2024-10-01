package com.exercise.gbtrain.controller;

import com.exercise.gbtrain.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@Controller
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/gettransaction")
    public ResponseEntity<Object> getTransaction() {
        var response = transactionService.getTransactions();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
