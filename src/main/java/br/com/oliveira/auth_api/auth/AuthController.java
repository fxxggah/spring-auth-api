package br.com.oliveira.auth_api.auth;

import br.com.oliveira.auth_api.dto.AuthResponse;
import br.com.oliveira.auth_api.dto.LoginRequest;
import br.com.oliveira.auth_api.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication){

        Map<String, Object> response = new HashMap<>();

        response.put("email", authentication.getName());
        response.put("roles", authentication.getAuthorities());

        return response;

    }

}
