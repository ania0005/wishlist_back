package de.aittr.project_wishlist.service.impl;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.domain.entity.Gift;
import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.domain.entity.Wishlist;
import de.aittr.project_wishlist.exceptions.ResourceNotFoundException;
import de.aittr.project_wishlist.repository.interfaces.GiftRepository;
import de.aittr.project_wishlist.repository.interfaces.UserRepository;
import de.aittr.project_wishlist.repository.interfaces.WishlistRepository;
import de.aittr.project_wishlist.service.interfaces.GiftService;
import de.aittr.project_wishlist.service.mapping.GiftMappingService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftServiceImpl implements GiftService {




    private final GiftRepository giftRepository;
    private final WishlistRepository wishlistRepository;
    private final GiftMappingService giftMappingService;
    private final UserRepository userRepository;

    public GiftServiceImpl(GiftRepository giftRepository, WishlistRepository wishlistRepository, GiftMappingService giftMappingService, UserRepository userRepository) {
        this.giftRepository = giftRepository;
        this.wishlistRepository = wishlistRepository;
        this.giftMappingService = giftMappingService;
        this.userRepository = userRepository;
    }

    @Override
    public GiftDto addGiftToWishlist(GiftDto giftDto, Long wishlistId, Authentication authentication) {
        giftDtoValidation(giftDto);
        User currentUser = userRepository.findByEmail(authentication.getName());
        Wishlist currentWishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
        if (currentWishlist.getUser().equals(currentUser)) {
            Gift savedGift = giftMappingService.mapDtoToEntity(giftDto);
            savedGift.setWishlist(currentWishlist);
            giftRepository.save(savedGift);
            return giftMappingService.mapEntityToDto(savedGift);
        }
        throw new AccessDeniedException("Wishlist does not belong to the user");
    }

    @Override
    public List<GiftDto> getAllGiftsFromWishlist(Long wishlistId, Authentication authentication) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        Wishlist currentWishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
        if (!currentWishlist.getUser().equals(currentUser)) {
            throw new AccessDeniedException("Wishlist does not belong to the user");
        }
        List<GiftDto> giftDtos = giftRepository.findByWishlistId(wishlistId).stream().map(giftMappingService::mapEntityToDto).collect(Collectors.toList());
        return giftDtos;
    }


    @Override
    public GiftDto getGiftById(Long giftId, Authentication authentication) {
        Gift gift = checkGiftOwnership(giftId, authentication);
        return giftMappingService.mapEntityToDto(gift);
    }

    @Override
    public GiftDto updateGiftById(Long giftId, GiftDto updatedGiftDto, Authentication authentication) {
        giftDtoValidation(updatedGiftDto);
        Gift existingGift = checkGiftOwnership(giftId, authentication);
        existingGift.setTitle(updatedGiftDto.getTitle());
        existingGift.setDescription(updatedGiftDto.getDescription());
        existingGift.setPrice(updatedGiftDto.getPrice());
        existingGift.setCurrency(updatedGiftDto.getCurrency());
        existingGift.setUrl(updatedGiftDto.getUrl());
        existingGift.setImgUrl(updatedGiftDto.getImgUrl());
        existingGift.setReserved(updatedGiftDto.isReserved());
        giftRepository.save(existingGift);
        return getGiftById(giftId, authentication);
    }

    @Override
    public void deleteGiftById(Long giftId, Authentication authentication) {
        checkGiftOwnership(giftId, authentication);
        giftRepository.deleteById(giftId);
    }


    private void giftDtoValidation(GiftDto giftDto) {
        if (giftDto.getTitle() == null || giftDto.getTitle().isEmpty() || giftDto.getPrice() == null || giftDto.getCurrency() == null || giftDto.getCurrency().isEmpty() || giftDto.getUrl() == null || giftDto.getUrl().isEmpty()) {
            throw new IllegalArgumentException("Title, URL, price and currency are required");
        }
    }

    private Gift checkGiftOwnership(Long giftId, Authentication authentication) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        Gift gift = giftRepository.findById(giftId).orElseThrow(() -> new ResourceNotFoundException("Gift not found"));
        Wishlist currentWishlist = gift.getWishlist();
        if (!currentWishlist.getUser().equals(currentUser)) {
            throw new AccessDeniedException("The gift does not belong to the user");
        }
        return gift;
    }

}


