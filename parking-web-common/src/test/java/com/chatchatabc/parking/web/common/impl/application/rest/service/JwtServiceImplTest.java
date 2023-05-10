package com.chatchatabc.parking.web.common.impl.application.rest.service;

import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtServiceImplTest {

    @Value("${server.jwt.secret}")
    private String secret;
    @Value("${server.jwt.expiration}")
    private String expiration;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtServiceImpl(secret);
    }


    @Test
    public void testGenerateToken() {
        String userId = "user1";
        String token = jwtService.generateToken(userId);
        assertNotNull(token);
    }

    @Test
    public void testValidateTokenAndGetUser() {
        User user = new User();
        user.setUserId("user1");

        String token = jwtService.generateToken(user.getUserId());
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

        User returnedUser = jwtService.validateTokenAndGetUser(token);
        assertNotNull(returnedUser);
        assertEquals(user.getUserId(), returnedUser.getUserId());

        verify(userRepository, times(1)).findByUserId(user.getUserId());
    }
}
