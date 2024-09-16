package com.exercise.gbtrain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "comparator")
public class ComparatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comparator_id", nullable = false, unique = true)
    private int id;

    @Column(name = "comparator_source", length = 5)
    private String source;

    @Column(name = "comparator_destination", length = 5)
    private String destination;

    @Column(name = "comparator_distance")
    private int distance;
}
