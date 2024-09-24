package com.exercise.gbtrain.entity;

import com.exercise.gbtrain.dto.farecalculator.request.FareCalculatorRequest;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int id;

    @Column(name = "source", nullable = false, length = 5)
    private String source;

    @Column(name = "destination", nullable = false, length = 5)
    private String destination;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDatetime;

    public static TransactionEntity formTransactionEntity(FareCalculatorRequest fareCalculatorRequest, float price) {
        TransactionEntity entity = new TransactionEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setSource(fareCalculatorRequest.getSource());
        entity.setDestination(fareCalculatorRequest.getDestination());
        entity.setPrice(price);
        entity.setType(fareCalculatorRequest.getType());
        entity.setCreateDatetime(now);
        return entity;
    }

}