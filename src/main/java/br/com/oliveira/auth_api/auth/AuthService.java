package br.com.oliveira.auth_api.auth;

import br.com.oliveira.auth_api.dto.request.RefreshRequest;
import br.com.oliveira.auth_api.dto.response.AuthResponse;
import br.com.oliveira.auth_api.dto.request.LoginRequest;
import br.com.oliveira.auth_api.dto.request.RegisterRequest;
import br.com.oliveira.auth_api.exception.InvalidCredentialsException;
import br.com.oliveira.auth_api.security.JwtService;
import br.com.oliveira.auth_api.user.User;
import br.com.oliveira.auth_api.user.UserRepository;
import br.com.oliveira.auth_api.user.UserType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(UserType.USER)
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(RefreshRequest request) {

        String refreshToken = request.getRefreshToken();

        String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new RuntimeException("Refresh token inválido");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new RuntimeException("Refresh token expirado ou inválido");
        }

        String newAccessToken = jwtService.generateToken(userDetails);

        return new AuthResponse(newAccessToken, refreshToken);
    }

}
