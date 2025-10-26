package de.aittr.project_wishlist.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Schema(name = "UserClass")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    @Schema(description = "User ID", example = "1")
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "Required")
    @Size(max = 30, message = "Max 30 symbols")
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only letters")
    @Schema(description = "User firstName", example = "James")
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 30, message = "Max 30 symbols")
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name must contain only letters")
    @Schema(description = "User lastName", example = "May")
    private String lastName;

    @Column(name = "email")
    @NotBlank(message = "Required")
    @Size(max = 30, message = "Max 30 symbols")
    @Email(message = "Invalid email format!")
    @Schema(description = "User email", example = "may@gmail.com")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Required")
    @Size(min = 60, max = 60)
    @Schema(description = "User password", example = "Password12#")
    private String password;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Wishlist> wishlists;

    @Column(name = "active")
    private boolean active;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private ConfirmationCode confirmationCode;


    public User(boolean active, ConfirmationCode confirmationCode, LocalDateTime createdAt, String email, String firstName, Long id, String lastName, String password, Set<Role> roles, Set<Wishlist> wishlists) {
        this.active = active;
        this.confirmationCode = confirmationCode;
        this.createdAt = createdAt;
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
        this.wishlists = wishlists;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ConfirmationCode getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(ConfirmationCode confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(Set<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return active == user.active && Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(createdAt, user.createdAt) && Objects.equals(roles, user.roles) && Objects.equals(wishlists, user.wishlists) && Objects.equals(confirmationCode, user.confirmationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, createdAt, roles, wishlists, active, confirmationCode);
    }

    @Override
    public String toString() {
        return String.format("User: ID - %d, First Name - %s, Second Name - %s, E-Mail - %s, Roles - %s", id, firstName, lastName, email, roles);
    }
}

