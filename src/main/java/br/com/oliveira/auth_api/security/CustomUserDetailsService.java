package br.com.oliveira.auth_api.security;

import br.com.oliveira.auth_api.user.User;
import br.com.oliveira.auth_api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Implementação do contrato UserDetailsService para integração com o Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Busca a entidade de usuário no banco pelo e-mail
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        // Retorna a implementação padrão de UserDetails do Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),       // Username utilizado na autenticação
                user.getPassword(),    // Hash da senha para validação do PasswordEncoder

                // Mapeia o tipo de usuário para GrantedAuthority com o prefixo 'ROLE_'
                // necessário para o controle de acesso (RBAC) do Spring Security
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()))
        );
    }

}