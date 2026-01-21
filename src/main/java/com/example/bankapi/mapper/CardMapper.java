package com.example.bankapi.mapper;

import com.example.bankapi.dto.card.CardReadDTO;
import com.example.bankapi.entity.Card;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public abstract class CardMapper {

	private static final String MASK_REGEX = ".(?=.{4})";

	@Mapping(source = "owner.id", target = "ownerId")
	@Mapping(target = "number", ignore = true)
	public abstract CardReadDTO toReadDto(Card entity);

	@AfterMapping
	public void afterToReadDtoMapping(@MappingTarget CardReadDTO dto, Card entity) {
		String maskedNumber = entity.getNumber().replaceAll(MASK_REGEX, "*");
		dto.setNumber(maskedNumber);
	}

}
