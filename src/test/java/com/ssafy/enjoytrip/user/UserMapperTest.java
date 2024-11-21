package com.ssafy.enjoytrip.user;

import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @AfterEach
    void cleanUp() {
        // TODO: 데이터베이스 초기화 로직 추가
    }

    @Test
    @DisplayName("Test Save User")
    void testSaveUser() {
        // Given
        User user = User.localUserBuilder()
                .username("testuser")
                .password("testpassword")
                .role(com.ssafy.enjoytrip.user.domain.Role.ROLE_USER)
                .name("Test User")
                .build();

        // When
        userMapper.save(user);

        // Then
        Optional<User> savedUser = userMapper.findByUsername("testuser");
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.get().getUsername());
    }

    @Test
    @DisplayName("Test Exists By Username")
    void testExistsByUsername() {
        // Given
        String username = "testuser";
        userMapper.save(User.localUserBuilder()
                .username(username)
                .password("testpassword")
                .role(com.ssafy.enjoytrip.user.domain.Role.ROLE_USER)
                .name("Test User")
                .build());

        // When
        boolean exists = userMapper.existsByUsername(username);

        // Then
        assertTrue(exists);
    }
}
