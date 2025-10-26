package de.aittr.project_wishlist.controller;


import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.service.interfaces.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Operation(summary = "Create new wishlist")
    @PostMapping
    public ResponseEntity<WishlistDto> createWishlist(@RequestBody WishlistDto wishlistDto, Authentication authentication) {
        WishlistDto savedWishlistDto = wishlistService.saveWishlist(wishlistDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWishlistDto);
    }

    @Operation(summary = "Get all wishlists")
    @GetMapping
    public ResponseEntity<List<WishlistDto>> getWishlists(Authentication authentication) {
        List<WishlistDto> wishlists = wishlistService.getAllWishlists(authentication);
        return ResponseEntity.ok(wishlists);
    }

    @Operation(summary = "Get wishlist by wishlist ID")
    @GetMapping("/{wishlistId}")
    public ResponseEntity<WishlistDto> getWishlistById(@PathVariable Long wishlistId, Authentication authentication) {
        WishlistDto wishlist = wishlistService.getWishlistById(wishlistId, authentication);
        return ResponseEntity.ok(wishlist);
    }

    @Operation(summary = "Update wishlist by wishlist ID")
    @PutMapping("/{wishlistId}")
    public ResponseEntity<WishlistDto> updateWishlistById(@RequestBody WishlistDto updatedWishlistDto, @PathVariable Long wishlistId, Authentication authentication) {
        wishlistService.updateWishlistById(updatedWishlistDto, wishlistId, authentication);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(wishlistService.getWishlistById(wishlistId, authentication));
    }

    @Operation(summary = "Delete wishlist by wishlist ID")
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<String> deleteWishlistById(@PathVariable Long wishlistId, Authentication authentication) {
        wishlistService.deleteWishlistById(wishlistId, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
