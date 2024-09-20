package com.exercise.gbtrain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "station")
public class StationEntity {

    @Id
    @Column(name = "station_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "station_name", unique = true, nullable = false, length = 5)
    private String stationName;

    @Column(name = "station_fullname", nullable = false, length = 255)
    private String stationFullname;

}