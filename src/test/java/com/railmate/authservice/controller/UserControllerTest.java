package com.railmate.authservice.controller;

import com.railmate.authservice.model.User;
import com.railmate.authservice.repository.UserRepository;
import com.railmate.authservice.service.CustomUserDetailsService;
import com.railmate.authservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock the service that returns the current user
    @MockBean
    private CustomUserDetailsService userService;

    // Mock JwtUtil so JwtAuthenticationFilter can be instantiated
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturnUserProfile() throws Exception {
        User user = new User();
        user.setEmail("rowm30@gmail.com");
        user.setName("Kumar Mayank Shrivastav");

        Mockito.when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("rowm30@gmail.com"))
                .andExpect(jsonPath("$.name").value("Kumar Mayank Shrivastav"));
    }

//    @Test
//    void shouldUpdateUserProfile() throws Exception {
//        Map<String, String> updates = Map.of(
//                "name", "Kumar Mayank Shrivastav",
//                "picture", "https://example.com/alice.jpg"
//        );
//
//        Mockito.when(userService.updateCurrentUser(updates))
//               .thenReturn(Map.of("status", "updated"));
//
//        mockMvc.perform(put("/profile/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{"
//                                + "\"name\":\"Kumar Mayank Shrivastav\","

//                                + "}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("updated"));
//    }
}
