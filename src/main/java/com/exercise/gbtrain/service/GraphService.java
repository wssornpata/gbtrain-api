package com.exercise.gbtrain.service;

import com.exercise.gbtrain.configuration.ExtendConfig;
import com.exercise.gbtrain.dto.farecalculator.FareCalculatorDistanceMap;
import com.exercise.gbtrain.entity.ExtendMappingEntity;
import com.exercise.gbtrain.entity.StationMappingEntity;
import com.exercise.gbtrain.exception.RouteNotFoundException;
import com.exercise.gbtrain.repository.StationMappingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraphService {

    private final ExtendConfig extendConfig;

    private final StationMappingRepository stationMappingRepository;

    private List<StationMappingEntity> mappings;

    public GraphService(ExtendConfig extendConfig, StationMappingRepository stationMappingRepository) {
        this.extendConfig = extendConfig;
        this.stationMappingRepository = stationMappingRepository;
    }

    @PostConstruct
    public void init() {
        loadMappings();
    }

    @Transactional(readOnly = true)
    public void loadMappings() {
        this.mappings = stationMappingRepository.findAll();
    }

    private FareCalculatorDistanceMap findShortestPath(Map<String, List<String>> graph, String source, String destination) {
        Queue<String> queue = new LinkedList<>();
        List<String> visited = new ArrayList<>();
        Map<String, Integer> distance = new HashMap<>();

        queue.add(source);
        visited.add(source);
        distance.put(source, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDistance = distance.get(current);

            if (current.equals(destination)) {
                return new FareCalculatorDistanceMap(currentDistance, distance);
            }

            for (String neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    distance.put(neighbor, currentDistance + 1);
                }
            }
        }
        throw new RouteNotFoundException("No route found between origin and destination");
    }

    public int getDistance(String source, String destination, int type, ExtendMappingEntity sourceMapping, ExtendMappingEntity destinationMapping) {
        Map<String, List<String>> graph = mappings.stream().collect(Collectors.groupingBy(StationMappingEntity::getFrom, Collectors.mapping(StationMappingEntity::getTo, Collectors.toList())));
        int distance = calculateFinalDistance(findShortestPath(graph, source, destination), type, sourceMapping, destinationMapping);
        return distance == 0 ? 1 : distance;
    }

    private int calculateFinalDistance(FareCalculatorDistanceMap fareCalculatorDistanceMap, int type, ExtendMappingEntity sourceMapping, ExtendMappingEntity destinationMapping) {
        int currentDistance = fareCalculatorDistanceMap.getDistance();

        if (currentDistance == -1) {
            throw new RouteNotFoundException("No route found between origin and destination");
        }

        if (type == 2) {
            Map<String, Integer> distanceMap = fareCalculatorDistanceMap.getDistanceMap();
            Integer startExtendA = distanceMap.get(extendConfig.getStartStationA());
            Integer startExtendB = distanceMap.get(extendConfig.getStartStationB());

            if (startExtendA != null && startExtendB != null) {
                return currentDistance;
            } else if (startExtendA != null) {
                if (sourceMapping != null && destinationMapping == null) {
                    return currentDistance - startExtendA;
                } else if (destinationMapping != null && sourceMapping == null) {
                    return startExtendA;
                }
            } else if (startExtendB != null) {
                if (sourceMapping != null && destinationMapping == null) {
                    return currentDistance - startExtendB;
                } else if (destinationMapping != null && sourceMapping == null) {
                    return startExtendB;
                }
            }
        }

        return currentDistance;
    }
}