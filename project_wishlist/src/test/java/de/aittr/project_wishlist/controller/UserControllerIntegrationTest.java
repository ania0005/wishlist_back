package de.aittr.project_wishlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.aittr.project_wishlist.domain.dto.UserDto;
import de.aittr.project_wishlist.domain.entity.Role;
import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.repository.interfaces.RoleRepository;
import de.aittr.project_wishlist.repository.interfaces.UserRepository;
import de.aittr.project_wishlist.service.interfaces.ConfirmationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private ConfirmationService confirmationService;

    private final String TEST_USER_NAME = "James";
    private final String TEST_USER_LASTNAME = "Brown";
    private final String TEST_USER_EMAIL = "james@gmail.com";
    private final String TEST_USER_PASSWORD = "password";

    @BeforeEach
    public void setUp() {

        if (userRepository.findByEmail(TEST_USER_EMAIL) == null) {

            User admin = new User();
            admin.setFirstName(TEST_USER_NAME);
            admin.setLastName(TEST_USER_LASTNAME);
            admin.setEmail(TEST_USER_EMAIL);
            String encodedPassword = new BCryptPasswordEncoder().encode(TEST_USER_PASSWORD);
            admin.setPassword(encodedPassword);

            Role adminRole = roleRepository.findByTitle("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }

            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
        }
    }

    @Test
    @WithMockUser(username = "james@gmail.com", roles = {"ADMIN"})
    public void testCreateUser() throws Exception {

        if (userRepository.findByEmail("john.doe@example.com") == null) {
            UserDto userDto = new UserDto();
            userDto.setFirstName("John");
            userDto.setLastName("Doe");
            userDto.setEmail("john.doe@example.com");
            userDto.setPassword("Password123#");

            mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                            .with(csrf())
                            .content(asJsonString(userDto))
                            .contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
    }


    @Test
    public void testActivateUser() throws Exception {
        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("john.doe@example.com");
        String encodedPassword = new BCryptPasswordEncoder().encode("Password123#");
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);

        String activationCode = confirmationService.generateConfirmationCode(newUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/activate?code=" + activationCode)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }




    @Test
    @WithMockUser(username = "james@gmail.com", roles = {"ADMIN"})
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", roles = {"ADMIN"})
    public void testGetUserInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/auth/me")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", roles = {"ADMIN"})
    public void testUpdateUser() throws Exception {
        UserDto updatedUser = new UserDto();
        updatedUser.setFirstName("UpdatedFirstName");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/auth/me")
                        .with(csrf())
                        .content(asJsonString(updatedUser))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/auth/me")
                        .with(csrf())
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
