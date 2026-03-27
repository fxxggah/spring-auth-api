package br.com.oliveira.auth_api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;

}
