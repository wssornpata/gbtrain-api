package com.exercise.gbtrain.service;

import com.exercise.gbtrain.entity.ExtendMappingEntity;
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

    @Transactional(readOnly = true)
    public List<StationMappingEntity> getMappings() {
        return stationMappingRepository.findAll();
    }

    private int findShortestPath(Map<String, List<String>> graph, String source, String destination, int type, ExtendMappingEntity sourceMapping, ExtendMappingEntity destinationMapping) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>(); // Use a Set for visited nodes
        Map<String, Integer> distance = new HashMap<>();

        queue.add(source);
        visited.add(source);
        distance.put(source, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDistance = distance.get(current);

            if (current.equals(destination)) {
                if (type == 2) {
                    Integer distanceN8 = distance.get("N8");
                    Integer distanceE9 = distance.get("E9");

                    if (distanceN8 != null && distanceE9 != null) {
                        return currentDistance;
                    }else if (distanceN8 != null) {
                        if(sourceMapping != null && destinationMapping == null) {
                            //ออกจาก Extend
                            return currentDistance-distanceN8;
                        } else if (destinationMapping != null && sourceMapping == null) {
                            //เข้า Extend
                            return distanceN8;
                        }
                    }else if (distanceE9 != null) {
                        if(sourceMapping != null && destinationMapping == null) {
                            //ออกจาก Extend
                            return currentDistance-distanceE9;
                        } else if (destinationMapping != null && sourceMapping == null) {
                            //เข้า Extend
                            return distanceE9;
                        }
                    }
                }
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

    public int getDistance(String source, String destination, int type, ExtendMappingEntity
            sourceMapping, ExtendMappingEntity destinationMapping) {
        Map<String, List<String>> graph = mappings.stream().collect(Collectors.groupingBy(StationMappingEntity::getFrom, Collectors.mapping(StationMappingEntity::getTo, Collectors.toList())));
        int distance = findShortestPath(graph, source, destination, type, sourceMapping, destinationMapping);
        return distance == 0 ? 1 : distance;
    }
//
//    private int calculateType2Distance(int currentDistance, Map<String, Integer> distance,
//                                       ExtendMappingEntity sourceMapping, ExtendMappingEntity destinationMapping) {
//        if (distance.get("N9") != null) {
//            return calculateDistanceForKey("N9", currentDistance, distance, sourceMapping, destinationMapping);
//        } else if (distance.get("E9") != null) {
//            return calculateDistanceForKey("E9", currentDistance, distance, sourceMapping, destinationMapping);
//        }
//        return currentDistance;
//    }
//
//    private int calculateDistanceForKey(String key, int currentDistance, Map<String, Integer> distance,
//                                        ExtendMappingEntity sourceMapping, ExtendMappingEntity destinationMapping) {
//        Integer keyDistance = distance.get(key);
//        if (keyDistance != null) {
//            if (sourceMapping != null && destinationMapping == null) {
//                return currentDistance - keyDistance;
//            } else if (sourceMapping == null && destinationMapping != null) {
//                return keyDistance;
//            }
//        }
//        return currentDistance;
//    }
}
