package de.aittr.project_wishlist.service.mapping;


import de.aittr.project_wishlist.domain.dto.UserDto;
import de.aittr.project_wishlist.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMappingService {


    @Mapping(target = "password", ignore = true)

    UserDto mapEntityToDto(User entity);

    User mapDtoToEntity(UserDto dto);

}
