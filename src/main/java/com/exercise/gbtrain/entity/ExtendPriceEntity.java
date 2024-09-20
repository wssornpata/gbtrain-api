package com.exercise.gbtrain.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "extendprice")
public class ExtendPriceEntity {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "extend_name", nullable = false, length = 100)
    private String extendName;

    @Column(name = "extend_price", nullable = false)
    private float extendPrice;
}
