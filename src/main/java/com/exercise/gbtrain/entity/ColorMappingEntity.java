package com.exercise.gbtrain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "colormapping")
public class ColorMappingEntity {

    @Id
    @Column(name = "color_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "color_name", unique = true, nullable = false, length = 100)
    private String colorName;

    @Column(name = "color_fullname", nullable = false, length = 255)
    private String colorFullname;

}