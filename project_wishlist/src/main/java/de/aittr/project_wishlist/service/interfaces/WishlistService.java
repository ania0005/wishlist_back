package de.aittr.project_wishlist.service.interfaces;

import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.domain.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface WishlistService {

    WishlistDto saveWishlist(WishlistDto wishlistDto, Authentication authentication);

    List<WishlistDto> getAllWishlists(Authentication authentication);

    WishlistDto getWishlistById(Long id, Authentication authentication);

    WishlistDto updateWishlistById(WishlistDto updatedWishlistDto, Long id, Authentication authentication);

    void deleteWishlistById(Long id, Authentication authentication);



}
