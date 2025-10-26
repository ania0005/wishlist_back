package de.aittr.project_wishlist.service.impl;

import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.domain.entity.Wishlist;
import de.aittr.project_wishlist.exceptions.ResourceNotFoundException;
import de.aittr.project_wishlist.repository.interfaces.WishlistRepository;
import de.aittr.project_wishlist.service.interfaces.UserService;
import de.aittr.project_wishlist.service.mapping.WishlistMappingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class WishlistServiceImplTest {


    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistMappingService wishlistMappingService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private User currentUser;
    private WishlistDto wishlistDto;
    private Wishlist wishlist;
    private Wishlist savedWishlist;
    private WishlistDto savedWishlistDto;
    private WishlistDto updatedWishlistDto;
    private Wishlist existingWishlist;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setEmail("user@example.com");

        wishlistDto = new WishlistDto();
        wishlistDto.setTitle("Birthday");
        wishlistDto.setDescription("Birthday list");
        wishlistDto.setEventDate(new Date());

        wishlist = new Wishlist();
        wishlist.setTitle("Birthday");
        wishlist.setDescription("Birthday list");
        wishlist.setEventDate(new Date());
        wishlist.setUser(currentUser);

        savedWishlist = new Wishlist();
        savedWishlist.setId(1L);
        savedWishlist.setTitle("Birthday");
        savedWishlist.setDescription("Birthday list");
        savedWishlist.setEventDate(new Date());
        savedWishlist.setUser(currentUser);

        savedWishlistDto = new WishlistDto();
        savedWishlistDto.setId(1L);
        savedWishlistDto.setTitle("Birthday");
        savedWishlistDto.setDescription("Birthday list");
        savedWishlistDto.setEventDate(new Date());

        updatedWishlistDto = new WishlistDto();
        updatedWishlistDto.setTitle("Updated Birthday");
        updatedWishlistDto.setDescription("Updated description");
        updatedWishlistDto.setEventDate(new Date());

        existingWishlist = new Wishlist();
        existingWishlist.setId(1L);
        existingWishlist.setTitle("Original Birthday");
        existingWishlist.setDescription("Original description");
        existingWishlist.setEventDate(new Date());
        existingWishlist.setUser(currentUser);
    }


    @Test
    void saveWishlist_Successful_ShouldReturnWishlistDto() {
        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(currentUser);
        when(wishlistMappingService.mapDtoToEntity(wishlistDto)).thenReturn(wishlist);
        when(wishlistRepository.save(wishlist)).thenReturn(savedWishlist);
        when(wishlistMappingService.mapEntityToDto(savedWishlist)).thenReturn(savedWishlistDto);

        WishlistDto result = wishlistService.saveWishlist(wishlistDto, authentication);

        assertEquals(savedWishlistDto, result);
    }


    // Негативный тест
    @Test
    void saveWishlist_MissingFields_ThrowException() {
        WishlistDto incompleteWishlistDto = new WishlistDto();
        incompleteWishlistDto.setTitle("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.saveWishlist(incompleteWishlistDto, authentication);
        });

        assertEquals("Title, description and event date are required", exception.getMessage());
    }


    @Test
    void getAllWishlists_ValidInput_Success() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("test@example.com", "password");

        User currentUser = new User();
        when(userService.findByEmail(anyString())).thenReturn(currentUser);

        Wishlist wishlist1 = new Wishlist();
        Wishlist wishlist2 = new Wishlist();
        List<Wishlist> wishlists = new ArrayList<>();
        wishlists.add(wishlist1);
        wishlists.add(wishlist2);
        when(wishlistRepository.findAllByUser(currentUser)).thenReturn(wishlists);

        WishlistDto wishlistDto1 = new WishlistDto();
        WishlistDto wishlistDto2 = new WishlistDto();
        when(wishlistMappingService.mapEntityToDto(wishlist1)).thenReturn(wishlistDto1);
        when(wishlistMappingService.mapEntityToDto(wishlist2)).thenReturn(wishlistDto2);

        List<WishlistDto> result = wishlistService.getAllWishlists(authentication);

        assertEquals(2, result.size());
    }


    @Test
    void getWishlistById_Successful_ShouldReturnWishlistDto() {
        Long wishlistId = 1L;
        String userEmail = "user@example.com";
        User currentUser = new User();
        currentUser.setEmail(userEmail);

        Wishlist wishlist = new Wishlist();
        wishlist.setId(wishlistId);
        wishlist.setUser(currentUser);

        WishlistDto wishlistDto = new WishlistDto();

        when(authentication.getName()).thenReturn(userEmail);
        when(userService.findByEmail(userEmail)).thenReturn(currentUser);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));
        when(wishlistMappingService.mapEntityToDto(wishlist)).thenReturn(wishlistDto);

        WishlistDto result = wishlistService.getWishlistById(wishlistId, authentication);

        assertEquals(wishlistDto, result);
    }


    // Негативный тест
    @Test
    void getWishlistById_WishlistNotBelongToUser_ThrowException() {
        Long wishlistId = 1L;
        String userEmail = "user@example.com";
        User currentUser = new User();
        currentUser.setEmail(userEmail);

        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");

        Wishlist wishlist = new Wishlist();
        wishlist.setId(wishlistId);
        wishlist.setUser(anotherUser);

        when(authentication.getName()).thenReturn(userEmail);
        when(userService.findByEmail(userEmail)).thenReturn(currentUser);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            wishlistService.getWishlistById(wishlistId, authentication);
        });

        assertEquals("The list does not belong to the user", exception.getMessage());
    }

    @Test
    void updateWishlistById_Successful_ShouldReturnUpdatedWishlistDto() {
        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(currentUser);
        when(wishlistRepository.findById(1L)).thenReturn(Optional.of(existingWishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(existingWishlist);
        when(wishlistMappingService.mapEntityToDto(any(Wishlist.class))).thenReturn(updatedWishlistDto);

        WishlistDto result = wishlistService.updateWishlistById(updatedWishlistDto, 1L, authentication);

        assertEquals(updatedWishlistDto, result);
        assertEquals("Updated Birthday", existingWishlist.getTitle());
        assertEquals("Updated description", existingWishlist.getDescription());
        assertEquals(updatedWishlistDto.getEventDate(), existingWishlist.getEventDate());
    }

    @Test
    void updateWishlistById_WishlistNotFound_ShouldThrowResourceNotFoundException() {
        when(wishlistRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            wishlistService.updateWishlistById(updatedWishlistDto, 1L, authentication);
        });

        assertEquals("Wishlist with id 1 does not exist", exception.getMessage());
    }


    // Негативный тест
    @Test
    void updateWishlistById_MissingFields_ThrowException() {
        WishlistDto incompleteWishlistDto = new WishlistDto();
        incompleteWishlistDto.setTitle("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.updateWishlistById(incompleteWishlistDto, 1L, authentication);
        });

        assertEquals("Title, description, and event date are required", exception.getMessage());
    }


    @Test
    void updateWishlistById_AccessDenied_ThrowException() {
        User anotherUser = new User();
        anotherUser.setEmail("another_user@example.com");
        existingWishlist.setUser(anotherUser);

        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(currentUser);
        when(wishlistRepository.findById(1L)).thenReturn(Optional.of(existingWishlist));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            wishlistService.updateWishlistById(updatedWishlistDto, 1L, authentication);
        });

        assertEquals("The list does not belong to the user", exception.getMessage());
    }


    @Test
    void deleteWishlistById() {
        Long wishlistId = 1L;
        String userEmail = "user@example.com";
        User currentUser = new User();
    }

    // Негативный тест

    @Test
    void deleteWishlistById_WishlistNotFound_ThrowException() {
        Long wishlistId = 1L;
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            wishlistService.deleteWishlistById(wishlistId, authentication);
        });

        assertEquals("Wishlist with id " + wishlistId + " does not exist", exception.getMessage());
    }

    @Test
    void deleteWishlistById_AccessDenied_ThrowException() {
        Long wishlistId = 1L;
        User anotherUser = new User();
        anotherUser.setEmail("another_user@example.com");

        Wishlist wishlist = new Wishlist();
        wishlist.setId(wishlistId);
        wishlist.setUser(anotherUser);

        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(currentUser);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            wishlistService.deleteWishlistById(wishlistId, authentication);
        });

        assertEquals("The list does not belong to the user", exception.getMessage());
    }
}
