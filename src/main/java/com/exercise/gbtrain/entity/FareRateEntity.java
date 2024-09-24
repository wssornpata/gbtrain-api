package com.exercise.gbtrain.entity;

import com.exercise.gbtrain.dto.priceadjustor.response.FareRateListResponse;
import com.exercise.gbtrain.dto.station.response.StationListResponse;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "farerate")
public class FareRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farerate_id", nullable = false)
    private int id;

    @Column(name = "farerate_distance", nullable = false, unique = true)
    private int distance;

    @Column(name = "farerate_price", nullable = false)
    private float price;

    @Column(name = "farerate_description", length = 255)
    private String description;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

//    @ManyToOne
//    @JoinColumn(name = "train_color", referencedColumnName = "train_color", nullable = false)
//    private ColorMappingEntity colorMapping;

    public static FareRateListResponse formFareRateEntity(FareRateEntity fareRateEntity) {
        FareRateListResponse response = new FareRateListResponse();
        response.setId(fareRateEntity.getId());
        response.setDistance(fareRateEntity.getDistance());
        response.setPrice(fareRateEntity.getPrice());
        response.setUpdateDateTime(fareRateEntity.getUpdateDatetime());
        return response;
    }
}