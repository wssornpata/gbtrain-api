package com.exercise.gbtrain.dto.type.response;

import com.exercise.gbtrain.entity.TypeEntity;
import lombok.Data;

@Data
public class TypeListResponse {
    private int id;
    private int type;
    private String description;

    public static TypeListResponse formTypeEntity(TypeEntity typeEntity) {
        TypeListResponse response = new TypeListResponse();
        response.setId(typeEntity.getId());
        response.setType(typeEntity.getId());
        response.setDescription(typeEntity.getDescription());
        return response;
    }
}
