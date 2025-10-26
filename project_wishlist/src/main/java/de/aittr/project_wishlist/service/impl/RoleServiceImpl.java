package de.aittr.project_wishlist.service.impl;

import de.aittr.project_wishlist.domain.entity.Role;
import de.aittr.project_wishlist.repository.interfaces.RoleRepository;
import de.aittr.project_wishlist.service.interfaces.RoleService;

public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRole() {
        Role role = roleRepository.findByTitle("ROLE_USER");
        if (role == null) {
            throw new RuntimeException("Data base doesn't contain ROLE_USER");
        }
        return role;
    }
}
