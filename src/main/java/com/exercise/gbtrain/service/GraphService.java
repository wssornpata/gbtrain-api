package com.exercise.gbtrain.service;

import com.exercise.gbtrain.entity.StationMappingEntity;
import com.exercise.gbtrain.repository.StationMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraphService {
    private final Logger logger = LoggerFactory.getLogger(GraphService.class);

    private final List<StationMappingEntity> mappings;

    public GraphService(StationMappingRepository stationMappingRepository) {
        mappings = stationMappingRepository.findAll();
    }

    private int findShortestPath(Map<String, List<String>> graph, String source, String destination) {
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
                return currentDistance;
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

    public int getDistance(String source, String destination) {
        Map<String, List<String>> graph = mappings.stream().collect(Collectors.groupingBy(StationMappingEntity::getFrom, Collectors.mapping(StationMappingEntity::getTo, Collectors.toList())));
        return findShortestPath(graph, source, destination);
    }

}
