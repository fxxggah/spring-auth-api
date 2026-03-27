package br.com.oliveira.auth_api.security;

import br.com.oliveira.auth_api.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY = "meuSegredoSuperSeguroParaJwtToken";

    public String generateToken(User user){

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userType", user.getUserType().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
