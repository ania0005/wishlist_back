package de.aittr.project_wishlist.service.interfaces;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.domain.entity.ShareLink;
import org.springframework.security.core.Authentication;

public interface ShareLinkService {

    ShareLink generateShareLink(Long wishlistId,  Authentication authentication);

    WishlistDto getWishlistByUuid(String uuid);

    GiftDto reserveGift(String uuid, Long giftId);
}