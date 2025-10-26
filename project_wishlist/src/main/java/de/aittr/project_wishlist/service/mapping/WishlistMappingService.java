package de.aittr.project_wishlist.service.mapping;

import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.domain.entity.Wishlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistMappingService {

    WishlistDto mapEntityToDto(Wishlist entity);

    Wishlist mapDtoToEntity(WishlistDto dto);

}
