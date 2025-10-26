package de.aittr.project_wishlist.repository.interfaces;

import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.domain.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findAllByUser(User user);
}
