package br.com.oliveira.auth_api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;

}
