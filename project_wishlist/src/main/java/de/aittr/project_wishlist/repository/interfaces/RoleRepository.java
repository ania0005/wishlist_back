package de.aittr.project_wishlist.repository.interfaces;

import de.aittr.project_wishlist.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByTitle(String title);
}