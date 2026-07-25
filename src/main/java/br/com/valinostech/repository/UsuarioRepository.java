package br.com.valinostech.repository;

import br.com.valinostech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Usado no Login e no início do Esqueci a Senha
    Usuario findByEmail(String email);

    // NOVA LINHA: Permite achar quem solicitou a redefinição através do token do link
    Usuario findByTokenRecuperacao(String tokenRecuperacao);
}