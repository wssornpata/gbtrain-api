package com.exercise.gbtrain.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "extend")
@Data
public class ExtendConfig {

    private String startStationA;
    private String endStationA;
    private String nameStationA;
    private String startStationB;
    private String endStationB;
    private String nameStationB;
}
