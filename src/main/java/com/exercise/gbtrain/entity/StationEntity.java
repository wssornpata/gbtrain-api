package com.exercise.gbtrain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "station")
@Data
public class StationEntity {

    @Id
    private String station_id;

    private String station_name;
}
