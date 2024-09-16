package com.exercise.gbtrain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "source", referencedColumnName = "station_name", nullable = false)
    private StationEntity source;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination", referencedColumnName = "station_name", nullable = false)
    private StationEntity destination;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDatetime;
}