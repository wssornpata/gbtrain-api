package com.exercise.gbtrain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "extendmapping")
public class ExtendMappingEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "extend_name", referencedColumnName = "extend_name", nullable = false)
    private ExtendPriceEntity extendPriceEntity;

    @Column(name = "extend_station", nullable = false, length = 5)
    private String stationName;
}
