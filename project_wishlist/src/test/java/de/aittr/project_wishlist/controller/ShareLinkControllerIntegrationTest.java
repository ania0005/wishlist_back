package de.aittr.project_wishlist.controller;

import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.domain.entity.ShareLink;
import de.aittr.project_wishlist.service.interfaces.ShareLinkService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShareLinkControllerIntegrationTest {

    @Mock
    private ShareLinkService shareLinkService;

    @InjectMocks
    private ShareLinkController shareLinkController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void testGenerateShareLink() {
        // Given
        Long wishlistId = 1L;
        Authentication authentication = mock(Authentication.class);
        ShareLink expectedShareLink = new ShareLink();
        when(shareLinkService.generateShareLink(wishlistId, authentication)).thenReturn(expectedShareLink);

        // When
        ResponseEntity<ShareLink> response = shareLinkController.generateShareLink(wishlistId, authentication);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedShareLink, response.getBody());

        // Verify that generateShareLink method is called with the correct arguments
        verify(shareLinkService).generateShareLink(eq(wishlistId), eq(authentication));
    }

    @Test
    void testReserveGift() {
        // Given
        Long giftId = 1L;
        GiftDto expectedGiftDto = new GiftDto();
        String uuid = UUID.randomUUID().toString();
        when(shareLinkService.reserveGift(eq(uuid), eq(giftId))).thenReturn(expectedGiftDto);

        // When
        ResponseEntity<GiftDto> response = shareLinkController.reserveGift(uuid, giftId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedGiftDto, response.getBody());
        verify(shareLinkService).reserveGift(eq(uuid), eq(giftId));
    }


    @Test
    void testGetWishlistByUuid() {
        // Given
        String uuid = "some-uuid";
        WishlistDto wishlistDto = new WishlistDto(null, LocalDate.now());
        when(shareLinkService.getWishlistByUuid(uuid)).thenReturn(wishlistDto);

        // When
        ResponseEntity<WishlistDto> response = shareLinkController.getWishlistByUuid(uuid);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wishlistDto, response.getBody());

        // Verify that getWishlistByUuid method is called with the correct argument
        verify(shareLinkService).getWishlistByUuid(eq(uuid));
    }
}
