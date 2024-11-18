//package com.ssafy.enjoytrip;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.ssafy.enjoytrip.user.domain.User;
//import com.ssafy.enjoytrip.user.mapper.UserMapper;
//import com.ssafy.enjoytrip.user.service.UserServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import static org.mockito.Mockito.when;
//
//class UserServiceTest {
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Mock
//    private UserMapper userRepository;
//
//    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void 회원가입_성공_테스트() {
//        // given
//        User user = new User();
//        user.addUsername("testuser");
//        user.addPassword("testpassword");
//
//        // when
//        userService.signUp(user);
//
//        // then
//        assertTrue(passwordEncoder.matches("testpassword", user.getPassword()));
//    }
//
//    @Test
//    void 로그인_성공_테스트() {
//        // given
//        User user = new User();
//        user.addUsername("testuser");
//        user.addPassword(passwordEncoder.encode("testpassword"));
//
//        // mock repository
//        when(userRepository.findByUsername("testuser")).thenReturn(user);
//
//        // when
//        boolean result = userService.signIn("testuser", "testpassword");
//
//        // then
//        assertTrue(result);
//    }
//
//    @Test
//    void 로그인_실패_테스트() {
//        // given
//        User user = new User();
//        user.addUsername("testuser");
//        user.addPassword(passwordEncoder.encode("wrongpassword"));
//
//        // mock repository
//        when(userRepository.findByUsername("testuser")).thenReturn(user);
//
//        // when
//        boolean result = userService.signIn("testuser", "testpassword");
//
//        // then
//        assertFalse(result);
//    }
//}