package br.com.valinostech.services;

import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Salvar usuário com senha criptografada
    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    // Buscar por e-mail para login
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Gerar token de recuperação de senha
    public String gerarTokenRecuperacao(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            String token = UUID.randomUUID().toString();
            usuario.setTokenRecuperacao(token);
            usuarioRepository.save(usuario);
            return token;
        }
        return null;
    }

    // Redefinir senha usando o token
    public boolean redefinirSenha(String token, String novaSenha) {
        Usuario usuario = usuarioRepository.findByTokenRecuperacao(token);
        if (usuario != null) {
            usuario.setSenha(passwordEncoder.encode(novaSenha));
            usuario.setTokenRecuperacao(null); // Limpa o token após o uso
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }
}
