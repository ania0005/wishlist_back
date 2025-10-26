package de.aittr.project_wishlist.controller;

import de.aittr.project_wishlist.domain.dto.WishlistDto;
import de.aittr.project_wishlist.repository.interfaces.WishlistRepository;
import de.aittr.project_wishlist.service.interfaces.WishlistService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WishlistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private WishlistRepository wishlistRepository;

    @Test
    @WithMockUser
    public void testCreateWishlist() throws Exception {
        WishlistDto mockWishlistDto = new WishlistDto();
        mockWishlistDto.setId(1L);
        when(wishlistService.saveWishlist(any(), any(Authentication.class))).thenReturn(mockWishlistDto);

        mockMvc.perform(post("/api/wishlists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    public void testGetWishlists() throws Exception {
        WishlistDto mockWishlistDto = new WishlistDto();
        mockWishlistDto.setId(1L);
        when(wishlistService.getAllWishlists(any(Authentication.class))).thenReturn(Collections.singletonList(mockWishlistDto));

        mockMvc.perform(get("/api/wishlists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser
    public void testGetWishlistById() throws Exception {
        WishlistDto mockWishlistDto = new WishlistDto();
        mockWishlistDto.setId(1L);
        when(wishlistService.getWishlistById(anyLong(), any(Authentication.class))).thenReturn(mockWishlistDto);

        mockMvc.perform(get("/api/wishlists/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    public void testUpdateWishlistById() throws Exception {
        WishlistDto mockWishlistDto = new WishlistDto();
        mockWishlistDto.setId(1L);
        when(wishlistService.updateWishlistById(any(), anyLong(), any(Authentication.class))).thenReturn(mockWishlistDto);

        mockMvc.perform(put("/api/wishlists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Wishlist Name\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }




    @Test
    @WithMockUser
    public void testDeleteWishlistById() throws Exception {
        mockMvc.perform(delete("/api/wishlists/1"))
                .andExpect(status().isNoContent());
    }
}
