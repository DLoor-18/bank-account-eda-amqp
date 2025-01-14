package ec.com.sofka;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private Environment environment;

    @InjectMocks
    private JwtService jwtService;

    private final String SECRET_KEY = "YmFzZTY0c2VjcmV0a2V5"; // Base64 encoded
    private final String TEST_USERNAME = "testuser";
    private final String TEST_TOKEN = "sample.jwt.token";
    private final UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            TEST_USERNAME, "password", Collections.emptyList());

    @BeforeEach
    void setUp() {
        when(environment.getProperty("secret.key")).thenReturn(SECRET_KEY);
        when(environment.getProperty("jwt.expiration")).thenReturn("3600000"); // 1 hour
    }

    @Test
    @DisplayName("when extracting username should return correct username from token")
    void whenExtractingUsername_shouldReturnCorrectUsername() {
        // Arrange
        Claims claims = Jwts.claims().setSubject(TEST_USERNAME);
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(jwtService.getSignIngKey(), SignatureAlgorithm.HS256)
                .compact();

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    @DisplayName("when generating token should return valid JWT token")
    void whenGeneratingToken_shouldReturnValidJwtToken() {
        // Arrange

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("when validating token should return true for valid token")
    void whenValidatingToken_shouldReturnTrueForValidToken() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        Boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("when validating token should return false for expired token")
    void whenValidatingToken_shouldReturnFalseForExpiredToken() {
        // Arrange
        Claims claims = Jwts.claims().setSubject(TEST_USERNAME);
        claims.setExpiration(new Date(System.currentTimeMillis() - 1000)); // Already expired
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .signWith(jwtService.getSignIngKey(), SignatureAlgorithm.HS256)
                .compact();

        // Act
        Boolean isValid = jwtService.isTokenValid(expiredToken, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("when extracting expiration date should return correct value")
    void whenExtractingExpirationDate_shouldReturnCorrectValue() {
        // Arrange
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000); // 1 hour from now
        Claims claims = Jwts.claims().setSubject(TEST_USERNAME).setExpiration(expirationDate);
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(jwtService.getSignIngKey(), SignatureAlgorithm.HS256)
                .compact();

        // Act
        Date extractedDate = jwtService.extractClaim(token, Claims::getExpiration);

        // Assert
        assertEquals(expirationDate, extractedDate);
    }


}