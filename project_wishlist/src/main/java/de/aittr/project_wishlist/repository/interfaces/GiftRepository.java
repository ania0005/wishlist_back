package de.aittr.project_wishlist.repository.interfaces;

import de.aittr.project_wishlist.domain.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftRepository extends JpaRepository<Gift, Long> {

    List<Gift> findByWishlistId(Long id);
}
