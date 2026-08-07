package br.com.valinostech.controller;

import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
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
    public String telaLogin(Model model) {
        model.addAttribute("usuarioForm", new Usuario());
        return "login"; // Renderiza login.html em templates
    }

    // Rota POST para processar o login do usuário com diagnóstico completo nos logs
    @PostMapping("/login")
    public String autenticarUsuario(@ModelAttribute("usuarioForm") Usuario usuarioForm,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        try {
            System.out.println(">>> TENTANDO LOGIN COM E-MAIL: " + usuarioForm.getEmail());

            if (usuarioForm.getEmail() == null || usuarioForm.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Por favor, digite o e-mail.");
                return "redirect:/login";
            }

            // 1. Busca o usuário no banco pelo e-mail informado (removendo espaços extras)
            Usuario usuarioBanco = usuarioRepository.findByEmail(usuarioForm.getEmail().trim());

            if (usuarioBanco == null) {
                System.out.println(">>> ALERTA: E-mail não encontrado no banco de dados!");
                redirectAttributes.addFlashAttribute("erro", "E-mail ou senha inválidos!");
                return "redirect:/login";
            }

            System.out.println(">>> USUÁRIO ENCONTRADO NO BANCO: " + usuarioBanco.getNome());

            // 2. Valida se a senha digitada confere com a criptografada no banco
            boolean senhaOk = passwordEncoder.matches(usuarioForm.getSenha(), usuarioBanco.getSenha());
            System.out.println(">>> A SENHA CONFERE? " + senhaOk);

            if (senhaOk) {
                // Salva o usuário na sessão para mantê-lo logado
                session.setAttribute("usuarioLogado", usuarioBanco);
                System.out.println(">>> LOGIN REALIZADO COM SUCESSO!");
                
                // Redireciona para a página principal da loja
                return "redirect:/";
            } else {
                System.out.println(">>> ALERTA: Senha incorreta para o e-mail informado.");
                redirectAttributes.addFlashAttribute("erro", "E-mail ou senha inválidos!");
                return "redirect:/login";
            }

        } catch (Exception e) {
            System.err.println(">>> ERRO CRÍTICO NO LOGIN: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao realizar login: " + e.getMessage());
            return "redirect:/login";
        }
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
            System.out.println(">>> TENTANDO CADASTRAR E-MAIL: " + usuario.getEmail());

            // Proteção de Segurança: Verifica se o e-mail já está cadastrado
            if (usuarioRepository.findByEmail(usuario.getEmail().trim()) != null) {
                redirectAttributes.addFlashAttribute("erro", "Este e-mail já está cadastrado na loja!");
                return "redirect:/cadastro";
            }

            // Garante que o tipo do usuário nunca seja nulo
            if (usuario.getTipo() == null || usuario.getTipo().isEmpty()) {
                usuario.setTipo("CLIENTE");
            }

            // Criptografa a senha antes de salvar no banco de dados
            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);

            // Salva o novo usuário no banco de dados
            usuarioRepository.save(usuario);
            System.out.println(">>> SUCESSO: Usuário salvo no banco com ID: " + usuario.getId());

            redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado com sucesso! Faça seu login.");
            return "redirect:/login";

        } catch (Exception e) {
            System.err.println(">>> ERRO CRÍTICO NO CADASTRO: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar o cadastro: " + e.getMessage());
            return "redirect:/cadastro";
        }
    }
}