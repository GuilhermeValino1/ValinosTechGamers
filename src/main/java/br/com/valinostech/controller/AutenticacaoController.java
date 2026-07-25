package br.com.valinostech.controller;

import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AutenticacaoController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Instancia o criptografador de senhas padrão do Spring Security
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Rota para exibir a página de Login
    @GetMapping("/login")
    public String telaLogin() {
        return "login"; // Renderiza login.html em templates
    }

    // Rota para exibir a página de Cadastro
    @GetMapping("/cadastro")
    public String telaCadastro(Model model) {
        model.addAttribute("usuarioForm", new Usuario());
        return "cadastro"; // Renderiza cadastro.html em templates
    }

    // Rota POST para processar o formulário de cadastro de novos clientes
    @PostMapping("/cadastro")
    public String registrarUsuario(@ModelAttribute("usuarioForm") Usuario usuario, 
                                   RedirectAttributes redirectAttributes) {
        try {
            // Proteção de Segurança: Verifica se o e-mail já está cadastrado
if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
    redirectAttributes.addFlashAttribute("erro", "Este e-mail já está cadastrado na loja!");
    return "redirect:/cadastro";
}

            // Criptografa a senha antes de salvar no banco de dados (Segurança Padrão Amazon)
            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);

            // Salva o novo usuário no MySQL
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado com sucesso! Faça seu login.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar o cadastro. Verifique os dados.");
            return "redirect:/cadastro";
        }
    }
}