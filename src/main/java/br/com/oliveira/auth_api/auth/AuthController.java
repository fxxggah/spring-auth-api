package br.com.oliveira.auth_api.auth;

import br.com.oliveira.auth_api.dto.response.AuthResponse;
import br.com.oliveira.auth_api.dto.request.LoginRequest;
import br.com.oliveira.auth_api.dto.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

}
