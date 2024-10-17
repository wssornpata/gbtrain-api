package com.exercise.gbtrain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "type")
public class TypeEntity {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", unique = true, nullable = false)
    private int type;

    @Column(name = "type_description", nullable = false, length = 255)
    private String description;
}
