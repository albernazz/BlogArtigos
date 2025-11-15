
package com.example.BlogArtigos.usuarios;

import com.example.BlogArtigos.gruposusuarios.GruposUsuarios;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity
@Getter
@Setter // Adicionado Setter para flexibilidade
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios implements UserDetails {

    @Id
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha; // Em um projeto real, isso deve ser HASHED (bcrypt)

    @ManyToOne // Muitos usuários podem pertencer a um grupo
    @JoinColumn(name = "grupo_id", nullable = false) // Chave estrangeira
    private GruposUsuarios grupo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 2. Aqui informamos ao Spring qual é o "Papel" (Role) do usuário
        // Usamos o nome do grupo que vem do banco (ex: 'ROLE_ADMIN', 'ROLE_AUTOR')
        // O Spring Security precisa que os papéis comecem com "ROLE_"
        // Seu Blog.sql já criou os ROLES assim, perfeito!
        return List.of(new SimpleGrantedAuthority(grupo.getNomeGrupo()));
    }

    @Override
    public String getPassword() {
        return this.senha; // 3. O Spring vai pegar a senha daqui
    }

    @Override
    public String getUsername() {
        return this.email; // 4. O "username" para o Spring será o email
    }

    // --- Métodos de status da conta (pode deixar true por padrão) ---
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}