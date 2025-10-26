package de.aittr.project_wishlist.controller;
import de.aittr.project_wishlist.domain.dto.GiftDto;
import de.aittr.project_wishlist.service.interfaces.GiftService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GiftControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GiftService giftService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    @WithMockUser
    public void testAddGiftToWishlist() throws Exception {
        GiftDto mockGiftDto = new GiftDto();
        mockGiftDto.setId(1L);
        when(giftService.addGiftToWishlist(any(), anyLong(), any(Authentication.class))).thenReturn(mockGiftDto);

        mockMvc.perform(post("/api/wishlists/1/gifts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    public void testGetAllGifts() throws Exception {
        GiftDto mockGiftDto = new GiftDto();
        mockGiftDto.setId(1L);

        when(giftService.getAllGiftsFromWishlist(anyLong(), any(Authentication.class))).thenReturn(Collections.singletonList(mockGiftDto));

        mockMvc.perform(get("/api/wishlists/1/gifts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser
    public void testGetGiftById() throws Exception {
        GiftDto mockGiftDto = new GiftDto();
        mockGiftDto.setId(1L);

        when(giftService.getGiftById(anyLong(), any(Authentication.class))).thenReturn(mockGiftDto);

        mockMvc.perform(get("/api/gifts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }



    @Test
    @WithMockUser
    public void testUpdateGiftByGiftId() throws Exception {
        GiftDto mockGiftDto = new GiftDto();
        mockGiftDto.setId(1L);
        when(giftService.updateGiftById(anyLong(), any(), any(Authentication.class))).thenReturn(mockGiftDto);

        mockMvc.perform(put("/api/gifts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    public void testDeleteGiftById() throws Exception {
        doNothing().when(giftService).deleteGiftById(anyLong(), any(Authentication.class));

        mockMvc.perform(delete("/api/gifts/1"))
                .andExpect(status().isNoContent());
    }
}
