package br.com.oliveira.auth_api.auth;
import br.com.oliveira.auth_api.dto.request.LoginRequest;
import br.com.oliveira.auth_api.dto.request.RefreshRequest;
import br.com.oliveira.auth_api.dto.request.RegisterRequest;
import br.com.oliveira.auth_api.dto.response.AuthResponse;
import br.com.oliveira.auth_api.security.JwtService;
import br.com.oliveira.auth_api.user.User;
import br.com.oliveira.auth_api.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveRegistrarUmUsuarioComSucesso() {
        // Arrange
        RegisterRequest request = new RegisterRequest("João", "joao@email.com", "senha123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("senha_criptografada");

        // Act
        authService.register(request);

        // Assert
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveLogarUmUsuarioComSucesso() {
        // Arrange
        LoginRequest request = new LoginRequest("joao@email.com", "senha123");
        User mockUser = User.builder()
                .email("joao@email.com")
                .password("senha_criptografada")
                .build();
        UserDetails mockUserDetails = mock(UserDetails.class);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(request.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(mockUser.getEmail())).thenReturn(mockUserDetails);
        when(jwtService.generateToken(mockUserDetails)).thenReturn("novo_access_token");
        when(jwtService.generateRefreshToken(mockUserDetails)).thenReturn("novo_refresh_token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("novo_access_token", response.getAccessToken());
        assertEquals("novo_refresh_token", response.getRefreshToken());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), mockUser.getPassword());
    }

    @Test
    void refreshToken() {
        // Arrange
        String tokenAntigo = "refresh_token_valido";
        RefreshRequest request = new RefreshRequest(tokenAntigo);
        UserDetails mockUserDetails = mock(UserDetails.class);
        String email = "joao@email.com";

        when(jwtService.extractUsername(tokenAntigo)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(mockUserDetails);
        when(jwtService.isTokenValid(tokenAntigo, mockUserDetails)).thenReturn(true);
        when(jwtService.generateToken(mockUserDetails)).thenReturn("novo_access_token_gerado");

        // Act
        AuthResponse response = authService.refreshToken(request);

        // Assert
        assertNotNull(response);
        assertEquals("novo_access_token_gerado", response.getAccessToken());
        assertEquals(tokenAntigo, response.getRefreshToken());

        verify(jwtService, times(1)).extractUsername(tokenAntigo);
        verify(jwtService, times(1)).isTokenValid(tokenAntigo, mockUserDetails);
        verify(jwtService, times(1)).generateToken(mockUserDetails);
    }
}