package com.ssafy.enjoytrip.user;

import com.ssafy.enjoytrip.user.domain.Role;
import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.dto.JoinDTO;
import com.ssafy.enjoytrip.user.mapper.UserMapper;
import com.ssafy.enjoytrip.user.service.JoinService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JoinServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private JoinService joinService;

    @Test
    @DisplayName("Test Join Process - successful")
    void testJoinProcessSuccess() {
        // Given
        JoinDTO joinDTO = JoinDTO.builder()
                .username("testuser")
                .password("testpassword")
                .role(Role.ROLE_USER)
                .name("Test User")
                .build();

        when(userMapper.existsByUsername("testuser")).thenReturn(false);
        when(userMapper.existsByName("Test User")).thenReturn(false);

        // When
        boolean result = joinService.joinProcess(joinDTO);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test Join Process - duplicate username")
    void testJoinProcessDuplicateUsername() {
        // Given
        JoinDTO joinDTO = JoinDTO.builder()
                .username("testuser")
                .password("testpassword")
                .role(Role.ROLE_USER)
                .name("Test User")
                .build();

        when(userMapper.existsByUsername("testuser")).thenReturn(true);

        // When
        boolean result = joinService.joinProcess(joinDTO);

        // Then
        assertFalse(result);
        verify(userMapper, never()).save(any(User.class));
    }
}
