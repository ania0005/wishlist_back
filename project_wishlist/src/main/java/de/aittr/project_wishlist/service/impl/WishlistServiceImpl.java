package de.aittr.project_wishlist.service.impl;

import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.domain.entity.Wishlist;
import de.aittr.project_wishlist.exceptions.ResourceNotFoundException;
import de.aittr.project_wishlist.repository.interfaces.WishlistRepository;
import de.aittr.project_wishlist.service.interfaces.UserService;
import de.aittr.project_wishlist.service.interfaces.WishlistService;
import de.aittr.project_wishlist.service.mapping.WishlistMappingService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMappingService wishlistMappingService;
    private final UserService userService;


    public WishlistServiceImpl(WishlistRepository wishlistRepository, WishlistMappingService wishlistMappingService, UserService userService) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistMappingService = wishlistMappingService;
        this.userService = userService;
    }

    @Override
    public WishlistDto saveWishlist(WishlistDto wishlistDto, Authentication authentication) {
        if (wishlistDto.getTitle() == null || wishlistDto.getTitle().isEmpty() ||
                wishlistDto.getEventDate() == null) {
            throw new IllegalArgumentException("Title and event date are required");
        }

        User currentUser = userService.findByEmail(authentication.getName());

        Wishlist wishlistEntity = wishlistMappingService.mapDtoToEntity(wishlistDto);
        wishlistEntity.setUser(currentUser);

        Wishlist savedWishlist = wishlistRepository.save(wishlistEntity);

        return wishlistMappingService.mapEntityToDto(savedWishlist);
    }


    @Override
    public List<WishlistDto> getAllWishlists(Authentication authentication) {

        User currentUser = userService.findByEmail(authentication.getName());

        List<Wishlist> wishlists = wishlistRepository.findAllByUser(currentUser);

        return wishlists.stream().map(wishlistMappingService::mapEntityToDto).collect(Collectors.toList());

    }

    @Override
    public WishlistDto getWishlistById(Long id, Authentication authentication) {

        User currentUser = userService.findByEmail(authentication.getName());

        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wishlist with id " + id + " does not exist"));

        if (!wishlist.getUser().equals(currentUser)) {
            throw new AccessDeniedException("The list does not belong to the user");
        }

        return wishlistMappingService.mapEntityToDto(wishlist);
    }

    @Override
    public WishlistDto updateWishlistById(WishlistDto updatedWishlistDto, Long id, Authentication authentication) {

        if (updatedWishlistDto.getTitle() == null || updatedWishlistDto.getTitle().isEmpty() ||
                updatedWishlistDto.getEventDate() == null) {
            throw new IllegalArgumentException("Title and event date are required");
        }


        Wishlist existingWishlist = wishlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wishlist with id " + id + " does not exist"));

        User currentUser = userService.findByEmail(authentication.getName());

        if (!existingWishlist.getUser().equals(currentUser)) {
            throw new AccessDeniedException("The list does not belong to the user");
        }

        existingWishlist.setTitle(updatedWishlistDto.getTitle());
        existingWishlist.setDescription(updatedWishlistDto.getDescription());
        existingWishlist.setEventDate(updatedWishlistDto.getEventDate());

        return wishlistMappingService.mapEntityToDto(wishlistRepository.save(existingWishlist));
    }

    @Override
    public void deleteWishlistById(Long id, Authentication authentication) {

        User currentUser = userService.findByEmail(authentication.getName());

        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wishlist with id " + id + " does not exist"));

        if (!wishlist.getUser().equals(currentUser)) {
            throw new AccessDeniedException("The list does not belong to the user");
        }

        wishlistRepository.delete(wishlist);
    }
}
