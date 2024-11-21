package com.ssafy.enjoytrip.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.user.controller.JoinController;
import com.ssafy.enjoytrip.user.domain.Role;
import com.ssafy.enjoytrip.user.dto.JoinDTO;
import com.ssafy.enjoytrip.user.service.JoinService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@WebMvcTest(JoinController.class)
@WithMockUser
@ContextConfiguration(classes = {JoinController.class})
public class JoinControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JoinService joinService;

    @Test
    @DisplayName("Test Join Process - valid input")
    void testJoinProcessWithValidInput() throws Exception {
        // Given
        JoinDTO joinDTO = JoinDTO.builder()
                .username("testuser")
                .password("testpassword")
                .role(Role.ROLE_USER)
                .name("Test User")
                .build();

        when(joinService.joinProcess(any(JoinDTO.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinDTO)))
                .andExpect(status().isOk());

        verify(joinService, times(1)).joinProcess(any(JoinDTO.class));
    }

    @Test
    @DisplayName("Test Join Process - invalid input")
    void testJoinProcessWithInvalidInput() throws Exception {
        // Given
        JoinDTO joinDTO = JoinDTO.builder()
                .username("")
                .password("testpassword")
                .role(com.ssafy.enjoytrip.user.domain.Role.ROLE_USER)
                .name("")
                .build();

        // When & Then
        mockMvc.perform(post("/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(joinService);
    }
}

