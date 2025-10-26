package de.aittr.project_wishlist.controller;


import de.aittr.project_wishlist.domain.dto.UserDto;
import de.aittr.project_wishlist.exceptions.ErrorResponse;
import de.aittr.project_wishlist.service.interfaces.ConfirmationService;
import de.aittr.project_wishlist.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ConfirmationService confirmationService;


    public UserController(ConfirmationService confirmationService, UserService userService) {
        this.confirmationService = confirmationService;
        this.userService = userService;
    }

    @Operation(summary = "Create new user (Register)")
    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("code") String code) {
        confirmationService.activateUser(code);
        return ResponseEntity.ok("User account activated successfully");

    }

    @Operation(summary = "Get list with all users")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get information about current User")
    @GetMapping("/auth/me")
    public ResponseEntity<UserDto> getUserInfo(Authentication authentication) {
        UserDto userInfo = userService.getUserInfo(authentication);
        return ResponseEntity.ok(userInfo);
    }

    @Operation(summary = "Update current user")
    @PutMapping("/auth/me")
    public ResponseEntity<UserDto> updateUser(Authentication authentication, @RequestBody UserDto updatedUserDto) {
        userService.updateUserDto(authentication, updatedUserDto);
        return ResponseEntity.ok(userService.getUserInfo(authentication));
    }

    @Operation(summary = "Delete current user (Delete account)")
    @DeleteMapping("/auth/me")
    public ResponseEntity<ErrorResponse> deleteUser(Authentication authentication, HttpServletResponse response) {
        userService.deleteUser(authentication);
        Cookie cookie = new Cookie("Access-Token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


