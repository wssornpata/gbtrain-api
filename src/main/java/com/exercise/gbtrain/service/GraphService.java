package com.exercise.gbtrain.service;

import com.exercise.gbtrain.entity.StationMappingEntity;
import com.exercise.gbtrain.repository.StationMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraphService {
    private final Logger logger = LoggerFactory.getLogger(GraphService.class);

    private final List<StationMappingEntity> mappings;
    private final StationMappingRepository stationMappingRepository;

    public GraphService(StationMappingRepository stationMappingRepository) {
        this.stationMappingRepository = stationMappingRepository;
        this.mappings = getMappings();
    }

    @Transactional (readOnly = true)
    public List<StationMappingEntity> getMappings() {
        return stationMappingRepository.findAll();
    }

    private int findShortestPath(Map<String, List<String>> graph, String source, String destination, int type) {
        Queue<String> queue = new LinkedList<>();
        Queue<String> visited = new LinkedList<>();
        Map<String, Integer> distance = new HashMap<>();

        queue.add(source);
        visited.add(source);
        distance.put(source, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDistance = distance.get(current);

            if (current.equals(destination)) {
                if (type == 2) {
                    //ต้องเอามาลบกับค่าสถานีลบส่วนต่อขยาย่าสุด
                    int minus = distance.get("N8") != null ?distance.get("N8") : 0;
                    return currentDistance - minus == 0 ? 1:currentDistance-minus;
                } else {
                    return currentDistance;
                }
            }

            for (String neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    distance.put(neighbor, currentDistance + 1);
                }
            }
        }

        return -1;
    }

    public int getDistance(String source, String destination, int type) {
        Map<String, List<String>> graph = mappings.stream().collect(Collectors.groupingBy(StationMappingEntity::getFrom, Collectors.mapping(StationMappingEntity::getTo, Collectors.toList())));
        return findShortestPath(graph, source, destination, type);
    }

}
