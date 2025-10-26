package de.aittr.project_wishlist.controller;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.domain.entity.ShareLink;
import de.aittr.project_wishlist.service.interfaces.ShareLinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wishlists")
public class ShareLinkController {

    private final ShareLinkService shareLinkService;

    public ShareLinkController(ShareLinkService shareLinkService) {
        this.shareLinkService = shareLinkService;
    }

    @PostMapping("/{wishlistId}/share")
    public ResponseEntity<ShareLink> generateShareLink(@PathVariable Long wishlistId, Authentication authentication) {
        ShareLink link = shareLinkService.generateShareLink(wishlistId, authentication);
        return ResponseEntity.ok(link);
    }

    @GetMapping("/share/{uuid}")
    public ResponseEntity<WishlistDto> getWishlistByUuid(@PathVariable String uuid) {
        WishlistDto wishlistDto = shareLinkService.getWishlistByUuid(uuid);
        return ResponseEntity.ok(wishlistDto);
    }

    @PutMapping("/share/{uuid}/reserve/{giftId}")
    public ResponseEntity<GiftDto> reserveGift(@PathVariable String uuid, @PathVariable Long giftId) {
        GiftDto reservedGift = shareLinkService.reserveGift(uuid, giftId);
        return ResponseEntity.ok(reservedGift);
    }

}