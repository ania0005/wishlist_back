package de.aittr.project_wishlist.service.impl;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.domain.entity.Gift;
import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.domain.entity.Wishlist;
import de.aittr.project_wishlist.exceptions.ResourceNotFoundException;
import de.aittr.project_wishlist.repository.interfaces.GiftRepository;
import de.aittr.project_wishlist.repository.interfaces.UserRepository;
import de.aittr.project_wishlist.repository.interfaces.WishlistRepository;
import de.aittr.project_wishlist.service.mapping.GiftMappingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class GiftServiceImplTest {

    @Mock
    private GiftRepository giftRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private GiftMappingService giftMappingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private GiftServiceImpl giftService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addGiftToWishlist_WithValidInput_ShouldReturnGiftDto() {

        GiftDto giftDto = new GiftDto();

        giftDto.setTitle("Test Title");
        giftDto.setPrice(new BigDecimal("400.00"));
        giftDto.setCurrency("USD");
        giftDto.setUrl("http://test.com");

        Long wishlistId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        Gift gift = new Gift();
        gift.setWishlist(wishlist);

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));
        when(giftMappingService.mapDtoToEntity(giftDto)).thenReturn(gift);
        when(giftRepository.save(gift)).thenReturn(gift);
        when(giftMappingService.mapEntityToDto(gift)).thenReturn(giftDto);


        GiftDto result = giftService.addGiftToWishlist(giftDto, wishlistId, authentication);


        assertNotNull(result);
    }

    //  Негативный тест
    @Test
    public void addGiftToWishlist_WithInvalidInput_ThrowsException() {
        GiftDto giftDto = new GiftDto();
        giftDto.setTitle("Test Title");
        giftDto.setPrice(new BigDecimal("400.00"));
        giftDto.setCurrency("USD");
        giftDto.setUrl("http://test.com");

        Long wishlistId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            giftService.addGiftToWishlist(giftDto, wishlistId, authentication);
        });
    }


    @Test
    public void getAllGiftsFromWishlist_WithValidInput_ShouldReturnListOfGiftDto() {

        Long wishlistId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        List<Gift> gifts = Collections.singletonList(new Gift());

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));
        when(giftRepository.findByWishlistId(wishlistId)).thenReturn(gifts);
        when(giftMappingService.mapEntityToDto(any())).thenReturn(new GiftDto());


        List<GiftDto> result = giftService.getAllGiftsFromWishlist(wishlistId, authentication);


        assertFalse(result.isEmpty());
    }

    //  Негативный тест
    @Test
    public void getAllGiftsFromWishlist_WithNonExistentWishlist_ThrowException() {
        Long wishlistId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            giftService.getAllGiftsFromWishlist(wishlistId, authentication);
        });
    }


    @Test
    public void getGiftById_WithValidInput_ShouldReturnGiftDto() {

        Long giftId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        Gift gift = new Gift();
        gift.setWishlist(wishlist);

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(giftRepository.findById(giftId)).thenReturn(Optional.of(gift));
        when(giftMappingService.mapEntityToDto(gift)).thenReturn(new GiftDto());

        GiftDto result = giftService.getGiftById(giftId, authentication);

        assertNotNull(result);
    }

    //  Негативный тест
    @Test
    public void getGiftById_WithInvalidInput_ThrowsException() {
        Long giftId = 1L;
        User user = new User();
        user.setEmail("test@example.com");

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(giftRepository.findById(giftId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            giftService.getGiftById(giftId, authentication);
        });
    }


    @Test
    public void updateGiftById_WithValidInput_ShouldReturnUpdatedGiftDto() {

        Long giftId = 1L;
        GiftDto updatedGiftDto = new GiftDto();
        updatedGiftDto.setTitle("Updated Title");
        updatedGiftDto.setPrice(new BigDecimal("200.00"));
        updatedGiftDto.setCurrency("EUR");
        updatedGiftDto.setUrl("http://updated.com");

        User user = new User();
        user.setEmail("test@example.com");

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        Gift gift = new Gift();
        gift.setWishlist(wishlist);

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(giftRepository.findById(giftId)).thenReturn(Optional.of(gift));
        when(giftRepository.save(gift)).thenReturn(gift);
        when(giftMappingService.mapEntityToDto(gift)).thenReturn(updatedGiftDto);

        GiftDto result = giftService.updateGiftById(giftId, updatedGiftDto, authentication);

        assertNotNull(result);
    }

    //  Негативный тест
    @Test
    public void updateGiftById_WithInvalidInput_ThrowsException() {
        Long giftId = 1L;

        GiftDto updatedGiftDto = new GiftDto();
        updatedGiftDto.setTitle("Updated Title");
        updatedGiftDto.setPrice(new BigDecimal("100"));
        updatedGiftDto.setCurrency("EUR");
        updatedGiftDto.setUrl("http://updated.com");

        User user = new User();
        user.setEmail("test@example.com");

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(giftRepository.findById(giftId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            giftService.updateGiftById(giftId, updatedGiftDto, authentication);
        });


    }


    @Test
    public void deleteGiftById_WithValidInput_ShouldDeleteGift() {

        Long giftId = 1L;

        User user = new User();
        user.setEmail("test@example.com");

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        Gift gift = new Gift();
        gift.setWishlist(wishlist);

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(giftRepository.findById(giftId)).thenReturn(Optional.of(gift));

        assertDoesNotThrow(() -> giftService.deleteGiftById(giftId, authentication));
    }

    //  Негативный тест
    @Test
    public void deleteGiftById_WithNonExistentGift_ThrowsException() {
        Long giftId = 1L;

        User user = new User();
        user.setEmail("test@example.com");

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(giftRepository.findById(giftId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            giftService.deleteGiftById(giftId, authentication);
        });
    }

}
