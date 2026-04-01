package br.com.oliveira.auth_api.dto.request;


import br.com.oliveira.auth_api.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nome obrigatório")
    @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
    private String name;

    @NotBlank(message = "Email obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotNull(message = "Tipo de usuário obrigatório")
    private UserType userType;

}
