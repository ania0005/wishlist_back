package de.aittr.project_wishlist.service.interfaces;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface GiftService {

    GiftDto addGiftToWishlist(GiftDto giftDto, Long wishlistId, Authentication authentication);

    List<GiftDto> getAllGiftsFromWishlist(Long wishlistId, Authentication authentication);

    GiftDto getGiftById(Long giftId, Authentication authentication);

    GiftDto updateGiftById(Long giftId, GiftDto giftDto, Authentication authentication);

    void deleteGiftById(Long giftId, Authentication authentication);
}
