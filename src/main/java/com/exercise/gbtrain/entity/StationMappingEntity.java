package com.exercise.gbtrain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "stationmapping")
public class StationMappingEntity {

    @Id
    @Column(name = "mapping_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mapping_from", nullable = false, length = 5)
    private String from;

    @Column(name = "mapping_to", nullable = false, length = 5)
    private String to;
}
