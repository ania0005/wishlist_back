package de.aittr.project_wishlist.service.interfaces;

import de.aittr.project_wishlist.domain.entity.User;

public interface EmailService {

    void sendConfirmationEmail(User user);

}
