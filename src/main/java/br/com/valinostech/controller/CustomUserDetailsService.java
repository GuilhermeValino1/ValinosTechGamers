package br.com.valinostech.controller;

import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(">>> TENTATIVA DE LOGIN PARA O E-MAIL: " + email);
        
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            System.out.println(">>> ERRO: Usuário não encontrado no banco!");
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }
        
        System.out.println(">>> SUCESSO: Usuário encontrado! Senha hash no banco: " + usuario.getSenha());
        
        // Lógica blindada: Garante que o prefixo ROLE_ exista apenas uma vez
        String tipo = (usuario.getTipo() != null) ? usuario.getTipo().toUpperCase() : "USER";
        String role = tipo.startsWith("ROLE_") ? tipo : "ROLE_" + tipo;
        
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        
        return new User(
            usuario.getEmail(), 
            usuario.getSenha(), 
            Collections.singletonList(authority)
        );
    }
}