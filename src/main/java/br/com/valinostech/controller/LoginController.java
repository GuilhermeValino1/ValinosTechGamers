package br.com.valinostech.controller;

import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // =========================================================================
    // COMENTADO PARA EVITAR CONFLITO (O AutenticacaoController já cuida do /login)
    // =========================================================================
    // @GetMapping("/login")
    // public String exibirLogin() {
    //     return "login"; 
    // }

    // =========================================================================
    // COMENTADO PARA EVITAR CONFLITO (O AutenticacaoController já cuida do /cadastro)
    // =========================================================================
    // @GetMapping("/cadastro")
    // public String exibirCadastro() {
    //     return "cadastro"; 
    // }

    // Lógica de Processamento do Cadastro
    @PostMapping("/cadastrar")
    public String cadastrarUsuario(Usuario usuario) {
        // Criptografa a senha antes de salvar (Segurança Profissional)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        
        usuario.setTipo("USER");
        usuarioRepository.save(usuario);
        return "redirect:/login?sucesso"; 
    }

    // Rota para a tela de Esqueci minha Senha
    @GetMapping("/esqueci-senha")
    public String exibirEsqueciSenha() {
        return "esqueci_senha"; // Você deve criar o arquivo esqueci_senha.html
    }
}