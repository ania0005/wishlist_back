package de.aittr.project_wishlist.service.interfaces;

import de.aittr.project_wishlist.domain.dto.UserDto;
import de.aittr.project_wishlist.domain.entity.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    UserDto getUserInfo(Authentication authentication);

    UserDto updateUserDto(Authentication authentication, UserDto updatedUserDto);

    void deleteUser(Authentication authentication);

    List<UserDto> getAllUsers();

    @Scheduled(fixedRate = 120000)
    void deleteExpiredUnconfirmedUsers();

    User findByEmail(String email);


}


