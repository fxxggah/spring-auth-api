package br.com.oliveira.auth_api.auth;

import br.com.oliveira.auth_api.dto.response.AuthResponse;
import br.com.oliveira.auth_api.dto.request.LoginRequest;
import br.com.oliveira.auth_api.dto.request.RegisterRequest;
import br.com.oliveira.auth_api.exception.InvalidCredentialsException;
import br.com.oliveira.auth_api.security.JwtService;
import br.com.oliveira.auth_api.user.User;
import br.com.oliveira.auth_api.user.UserRepository;
import br.com.oliveira.auth_api.user.UserType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
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

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

}
