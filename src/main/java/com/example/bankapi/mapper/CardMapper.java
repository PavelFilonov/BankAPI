package com.example.bankapi.mapper;

import com.example.bankapi.dto.CardReadDTO;
import com.example.bankapi.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface CardMapper {

	@Mapping(source = "owner.id", target = "ownerId")
	CardReadDTO toReadDto(Card entity);

}
