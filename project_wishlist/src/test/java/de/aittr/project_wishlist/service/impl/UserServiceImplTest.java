package de.aittr.project_wishlist.service.impl;

import de.aittr.project_wishlist.domain.dto.UserDto;
import de.aittr.project_wishlist.domain.entity.Role;
import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.exceptions.UserAlreadyExistsException;
import de.aittr.project_wishlist.repository.interfaces.RoleRepository;
import de.aittr.project_wishlist.repository.interfaces.UserRepository;
import de.aittr.project_wishlist.service.mapping.UserMappingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private UserMappingService userMappingService;

    @Mock
    private Authentication authentication;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveUser_WithValidInput_ShouldReturnSavedUserDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Leon");
        userDto.setLastName("Davis");
        userDto.setEmail("leon@davis.com");
        userDto.setPassword("password1!");

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        Role role = new Role();
        role.setTitle("ROLE_USER");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);
        when(roleRepository.findByTitle("ROLE_USER")).thenReturn(role);
        when(encoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userMappingService.mapDtoToEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMappingService.mapEntityToDto(user)).thenReturn(userDto);

        UserDto savedUserDto = userService.saveUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals("leon@davis.com", savedUserDto.getEmail());
    }

    //  Негативный тест
    @Test
    public void saveUser_WithExistingEmail_ThrowException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("existing.email@example.com");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(new User());

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userDto));
    }

    @Test
    public void getUserInfo_WithValidAuthentication_ShouldReturnUserDto() {
        User user = new User();
        user.setEmail("user@example.com");
        UserDto userDto = new UserDto();
        userDto.setEmail("user@example.com");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(userMappingService.mapEntityToDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserInfo(authentication);

        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }

    //  Негативный тест
    @Test
    public void getUserInfo_WithInvalidAuthentication_ThrowException() {
        when(authentication.getName()).thenReturn("unknown@example.com");
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> userService.getUserInfo(authentication));
    }


    @Test
    public void updateUserDto_WithValidInput_ShouldReturnUpdatedUserDto() {
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setFirstName("Updated");
        updatedUserDto.setLastName("User");

        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMappingService.mapEntityToDto(user)).thenReturn(updatedUserDto);

        UserDto result = userService.updateUserDto(authentication, updatedUserDto);

        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
        assertEquals("User", result.getLastName());
    }


    //  Негативный тест
    @Test
    public void updateUserDto_WithNonExistentUser_ThrowException() {

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setFirstName("Updated");
        updatedUserDto.setLastName("User");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUserDto(authentication, updatedUserDto));
    }

    @Test
    public void deleteUser_WithValidAuthentication_ShouldDeleteUser() {
        User user = new User();
        user.setEmail("user@example.com");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        assertDoesNotThrow(() -> userService.deleteUser(authentication));
        verify(userRepository, times(1)).delete(user);
    }


    //  Негативный тест
    @Test
    public void deleteUser_WithInvalidAuthentication_ThrowException() {
        User user = new User();
        user.setEmail("user@example.com");


        when(authentication.getName()).thenReturn("another_user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);


        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(authentication));


        verify(userRepository, never()).delete(user);

    }

    @Test
    public void getAllUsers_ShouldReturnListOfUserDtos() {
        User user = new User();
        user.setEmail("user@example.com");
        List<User> users = Collections.singletonList(user);

        UserDto userDto = new UserDto();
        userDto.setEmail("user@example.com");

        when(userRepository.findAll()).thenReturn(users);
        when(userMappingService.mapEntityToDto(any(User.class))).thenReturn(userDto);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("user@example.com", result.get(0).getEmail());
    }
}