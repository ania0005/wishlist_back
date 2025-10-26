package de.aittr.project_wishlist.controller;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.service.interfaces.GiftService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GiftController {

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @Operation(summary = "Add gift to wishlist")
    @PostMapping("/wishlists/{wishlistId}/gifts")
    public ResponseEntity<GiftDto> addGiftToWishlist(@RequestBody GiftDto giftDto, @PathVariable Long wishlistId, Authentication authentication) {
        GiftDto savedGiftDto = giftService.addGiftToWishlist(giftDto, wishlistId, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGiftDto);
    }

    @Operation(summary = "Get all gifts by Wishlist ID")
    @GetMapping("/wishlists/{wishlistId}/gifts")
    public ResponseEntity<List<GiftDto>> getAllGifts(@PathVariable Long wishlistId, Authentication authentication) {
        List<GiftDto> giftDtos = giftService.getAllGiftsFromWishlist(wishlistId, authentication);
        return ResponseEntity.ok(giftDtos);
    }

    @Operation(summary = "Get gift by Gift ID")
    @GetMapping("/gifts/{giftId}")
    public ResponseEntity<GiftDto> getGiftById(@PathVariable Long giftId, Authentication authentication) {
        GiftDto giftDto = giftService.getGiftById(giftId, authentication);
        return ResponseEntity.ok(giftDto);
    }

    @Operation(summary = "Update gift by Gift ID")
    @PutMapping("/gifts/{giftId}")
    public ResponseEntity<GiftDto> updateGiftByGiftId(@RequestBody GiftDto giftDto, @PathVariable Long giftId, Authentication authentication) {
        GiftDto updatedGiftDto = giftService.updateGiftById(giftId, giftDto, authentication);
        return ResponseEntity.ok(updatedGiftDto);

    }

    @Operation(summary = "Delete gift by Gift ID")
    @DeleteMapping("/gifts/{giftId}")
    public ResponseEntity<String> deleteGiftById(@PathVariable Long giftId, Authentication authentication) {
        giftService.deleteGiftById(giftId, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
