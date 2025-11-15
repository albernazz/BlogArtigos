
package com.example.BlogArtigos.security;

import com.example.BlogArtigos.usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuariosRepository repository;

    // O Spring Security chama este método ao tentar autenticar
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // "username" para o Spring é o nosso "email"
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));
    }
}