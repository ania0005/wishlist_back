package de.aittr.project_wishlist.service.impl;


import de.aittr.project_wishlist.domain.dto.UserDto;
import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.exceptions.UserAlreadyExistsException;
import de.aittr.project_wishlist.repository.interfaces.RoleRepository;
import de.aittr.project_wishlist.repository.interfaces.UserRepository;
import de.aittr.project_wishlist.repository.interfaces.ConfirmationCodeRepository;
import de.aittr.project_wishlist.service.interfaces.EmailService;
import de.aittr.project_wishlist.service.interfaces.UserService;
import de.aittr.project_wishlist.service.mapping.UserMappingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMappingService userMappingService;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final EmailService emailService;

    public UserServiceImpl(BCryptPasswordEncoder encoder, RoleRepository roleRepository, UserMappingService userMappingService, UserRepository userRepository, EmailService emailService, ConfirmationCodeRepository confirmationCodeRepository) {
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userMappingService = userMappingService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.confirmationCodeRepository = confirmationCodeRepository;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {

        userDto.setId(null);

        if (userDto.getFirstName() == null || userDto.getFirstName().isEmpty() ||
                userDto.getEmail() == null || userDto.getEmail().isEmpty() ||
                userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Fields first name, e-mail and password are required");
        }

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
        }

        userDto.setRoles(Collections.singleton(roleRepository.findByTitle("ROLE_USER")));
        validatePassword(userDto.getPassword());
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        userDto.setActive(false);
        User user = userMappingService.mapDtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        emailService.sendConfirmationEmail(user);

        return userMappingService.mapEntityToDto(savedUser);
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one letter");
        }
        if (!password.matches(".*[.,?!@#$%^&+=].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character (.,?!@#$%^&+=)");
        }
    }

    @Override
    public UserDto getUserInfo(Authentication authentication) {

        String username = authentication.getName();
        User currentUser = findByEmail(username);

        if (currentUser == null) {
            throw new NoSuchElementException("User not found");
        }

        return userMappingService.mapEntityToDto(currentUser);
    }

    @Override
    public UserDto updateUserDto(Authentication authentication, UserDto updatedUserDto) {

        if (updatedUserDto.getFirstName() == null || updatedUserDto.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        User currentUser = findByEmail(authentication.getName());
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        Long id = currentUser.getId();

        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setFirstName(updatedUserDto.getFirstName());
            existingUser.setLastName(updatedUserDto.getLastName());
            return userMappingService.mapEntityToDto(userRepository.save(existingUser));
        } else {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
    }

    @Override
    public void deleteUser(Authentication authentication) {
        User currentUser = findByEmail(authentication.getName());

        if (currentUser != null) {
            userRepository.delete(currentUser);
        } else {
            throw new NoSuchElementException("Пользователь не найден");
        }
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = userMappingService.mapEntityToDto(user);
            userDtos.add(userDto);
        }

        return userDtos;
    }

    @Override
    @Scheduled(fixedRate = 1440000)
    public void deleteExpiredUnconfirmedUsers() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(2);
        List<User> unconfirmedUsers = userRepository.findByActiveFalseAndCreatedAtBefore(cutoffTime);
        userRepository.deleteAll(unconfirmedUsers);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
