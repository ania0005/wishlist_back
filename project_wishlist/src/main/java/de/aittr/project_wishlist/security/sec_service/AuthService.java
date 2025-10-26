package de.aittr.project_wishlist.security.sec_service;

import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.exceptions.AuthException;
import de.aittr.project_wishlist.security.sec_dto.TokenResponseDto;
import de.aittr.project_wishlist.service.interfaces.UserService;
import de.aittr.project_wishlist.service.mapping.UserMappingService;
import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final Map<String, String> refreshStorage;
    private final BCryptPasswordEncoder encoder;
    private final UserMappingService userMappingService;

    public AuthService(BCryptPasswordEncoder encoder, TokenService tokenService, UserService userService, UserMappingService userMappingService) {
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.userService = userService;
        this.refreshStorage = new HashMap<>();
        this.userMappingService = userMappingService;
    }

    public TokenResponseDto login(@NonNull User inboundUser) throws AuthException {
        String username = inboundUser.getEmail();
        User foundUser = userService.findByEmail(username);

        if (foundUser == null) {
            throw new AuthException("User not found");
        }

        if (!isRegistrationConfirmed(foundUser)) {
            throw new AuthException("E-mail confirmation was not completed");
        }

        if (encoder.matches(inboundUser.getPassword(), foundUser.getPassword())) {
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshStorage.put(username, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken, userMappingService.mapEntityToDto(foundUser));
        } else {
            throw new AuthException("Password is incorrect");
        }
    }

    public TokenResponseDto getAccessToken(@NonNull String inboundRefreshToken) {
        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String savedRefreshToken = refreshStorage.get(username);

        if (inboundRefreshToken.equals(savedRefreshToken)) {
            User user = userService.findByEmail(username);
            String accessToken = tokenService.generateAccessToken(user);
            return new TokenResponseDto(accessToken, null);
        }
        return new TokenResponseDto(null, null);
    }


    private boolean isRegistrationConfirmed(User user) {
        return user.isActive();
    }


}
