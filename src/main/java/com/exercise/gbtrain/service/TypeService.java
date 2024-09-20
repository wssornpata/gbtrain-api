package com.exercise.gbtrain.service;

import com.exercise.gbtrain.dto.station.response.StationListResponse;
import com.exercise.gbtrain.dto.type.response.TypeListResponse;
import com.exercise.gbtrain.entity.StationEntity;
import com.exercise.gbtrain.entity.TypeEntity;
import com.exercise.gbtrain.repository.TypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeService {
    private final Logger logger = LoggerFactory.getLogger(TypeService.class);
    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<TypeListResponse> getTypeList() {
        List<TypeEntity> typeEntityList = typeRepository.findAll();
        return wrapperTypeListResponse(typeEntityList);
    }

    private List<TypeListResponse> wrapperTypeListResponse(List<TypeEntity> typeEntityList){
        return typeEntityList.stream().map(TypeListResponse::formTypeEntity).collect(Collectors.toList());
    }
}
