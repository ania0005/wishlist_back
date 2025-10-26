package de.aittr.project_wishlist.service.mapping;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.domain.entity.Gift;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GiftMappingService {

    GiftDto mapEntityToDto(Gift entity);

    Gift mapDtoToEntity(GiftDto dto);
}
