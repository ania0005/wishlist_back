package de.aittr.project_wishlist.repository.interfaces;

import de.aittr.project_wishlist.domain.entity.ConfirmationCode;
import de.aittr.project_wishlist.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    ConfirmationCode findByCode(String code);

    @Query("SELECT c FROM ConfirmationCode c WHERE c.user = ?1")
    ConfirmationCode findByUser(User user);


}
