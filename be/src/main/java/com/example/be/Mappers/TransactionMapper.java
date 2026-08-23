package com.example.be.Mappers;

import org.springframework.stereotype.Component;
import com.example.be.Entities.TransactionEntity;
import com.example.be.DTOs.TransactionDTO;

@Component
public class TransactionMapper {
    public TransactionDTO toDTO(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAmount(entity.getAmount());
        return dto;
    }

    public TransactionEntity toEntity(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }
        TransactionEntity entity = new TransactionEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAmount(dto.getAmount());
        return entity;
    }
}
