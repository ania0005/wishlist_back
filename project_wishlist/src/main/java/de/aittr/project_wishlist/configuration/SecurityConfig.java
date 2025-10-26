package de.aittr.project_wishlist.configuration;

import de.aittr.project_wishlist.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenFilter filter;

    public SecurityConfig(TokenFilter filter) {
        this.filter = filter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x -> x
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(x -> x
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs.yaml")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register", "/api/users/login", "/api/users/access", "/api/users/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/activate").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/auth/me").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/users/auth/me").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/auth/me").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/wishlists").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/wishlists").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/wishlists/{wishlistId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/wishlists/{wishlistId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/wishlists/{wishlistId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/wishlists/{wishlistId}/gifts").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/wishlists/{wishlistId}/gifts").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/gifts/{giftId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/gifts/{giftId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/gifts/{giftId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "api/wishlists/{wishlistId}/share/").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET,"api/wishlists/share/{uuid}").permitAll()
                        .requestMatchers(HttpMethod.PUT,"api/wishlists/share/{uuid}/reserve/{giftId}").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
