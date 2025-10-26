package de.aittr.project_wishlist.service.interfaces;

import de.aittr.project_wishlist.domain.entity.User;

public interface ConfirmationService {

    String generateConfirmationCode(User user);

    boolean activateUser(String code);

}
