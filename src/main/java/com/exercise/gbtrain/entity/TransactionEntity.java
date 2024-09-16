package com.exercise.gbtrain.entity;

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
    private int transactionId;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "source", referencedColumnName = "station_name", nullable = false)
//    private StationEntity source;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "destination", referencedColumnName = "station_name", nullable = false)
//    private StationEntity destination;

    @Column(name = "source", nullable = false, length = 5)
    private String source;

    @Column(name = "destination", nullable = false, length = 5)
    private String destination;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "train_color", nullable = false, length = 100)
    private String trainColor;

    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDatetime;
}