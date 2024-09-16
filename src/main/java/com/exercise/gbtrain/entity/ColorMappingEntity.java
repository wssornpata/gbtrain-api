package com.exercise.gbtrain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "colormapping")
public class ColorMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colormapping_id", nullable = false, unique = true)
    private int id;

    @Column(name = "train_color", nullable = false, length = 100)
    private String trainColor;

    @Column(name = "provider_name", nullable = false, length = 5)
    private String providerName;
}