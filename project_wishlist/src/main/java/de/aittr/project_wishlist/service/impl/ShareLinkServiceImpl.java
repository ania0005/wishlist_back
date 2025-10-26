package de.aittr.project_wishlist.service.impl;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.domain.entity.Gift;
import de.aittr.project_wishlist.domain.entity.ShareLink;
import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.domain.entity.Wishlist;
import de.aittr.project_wishlist.exceptions.ResourceNotFoundException;
import de.aittr.project_wishlist.repository.interfaces.GiftRepository;
import de.aittr.project_wishlist.repository.interfaces.ShareLinkRepository;
import de.aittr.project_wishlist.repository.interfaces.WishlistRepository;
import de.aittr.project_wishlist.service.interfaces.ShareLinkService;
import de.aittr.project_wishlist.service.interfaces.UserService;
import de.aittr.project_wishlist.service.mapping.GiftMappingService;
import de.aittr.project_wishlist.service.mapping.WishlistMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class ShareLinkServiceImpl implements ShareLinkService {

    private final ShareLinkRepository shareLinkRepository;
    private final WishlistRepository wishlistRepository;
    private final WishlistMappingService wishlistMappingService;
    private final GiftRepository giftRepository;
    private final GiftMappingService giftMappingService;
    private final UserService userService;

    @Autowired
    public ShareLinkServiceImpl(ShareLinkRepository shareLinkRepository, WishlistRepository wishlistRepository, WishlistMappingService wishlistMappingService, GiftRepository giftRepository, GiftMappingService giftMappingService, UserService userService) {
        this.shareLinkRepository = shareLinkRepository;
        this.wishlistRepository = wishlistRepository;
        this.wishlistMappingService = wishlistMappingService;
        this.giftRepository = giftRepository;
        this.giftMappingService = giftMappingService;
        this.userService = userService;
    }

    @Override
    public ShareLink generateShareLink(Long wishlistId, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
        if (!wishlist.getUser().equals(currentUser)) {
            throw new AccessDeniedException("The list does not belong to the user");
        }
        String uuid = UUID.randomUUID().toString();
        ShareLink shareLink = new ShareLink(uuid, wishlist);
        shareLinkRepository.save(shareLink);
        return shareLink;
    }

    @Override
    public WishlistDto getWishlistByUuid(String uuid) {
        ShareLink shareLink = shareLinkRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Share link not found"));
        Wishlist wishlist = shareLink.getWishlist();

        List<Gift> sortedGifts = wishlist.getGifts().stream()
                .sorted(Comparator.comparing(Gift::isReserved))
                .toList();
        wishlist.setGifts(new HashSet<>(sortedGifts));

        return wishlistMappingService.mapEntityToDto(wishlist);
    }

    @Override
    public GiftDto reserveGift(String uuid, Long giftId) {

        ShareLink shareLink = shareLinkRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Share link not found"));
        Wishlist wishlist = shareLink.getWishlist();
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new ResourceNotFoundException("Gift not found"));

        if (!wishlist.getGifts().contains(gift)) {
            throw new AccessDeniedException("Gift does not belong to the specified wishlist");
        }

        gift.setReserved(!gift.isReserved());

      GiftDto giftDto =  giftMappingService.mapEntityToDto(giftRepository.save(gift));

        return giftDto;
    }
}