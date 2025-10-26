package de.aittr.project_wishlist.repository.interfaces;

import de.aittr.project_wishlist.domain.entity.ShareLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {

    Optional<ShareLink> findByUuid(String uuid);

}