package br.com.valinostech.controller;

import br.com.valinostech.model.Produto;
import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.ProdutoRepository;
import br.com.valinostech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String exibirVitrine(@RequestParam(value = "nome", required = false) String nome, 
                                Model model, Authentication authentication) {
        
        if (nome != null && !nome.isEmpty()) {
            model.addAttribute("produtos", produtoRepository.findByNomeContainingIgnoreCase(nome));
        } else {
            model.addAttribute("produtos", produtoRepository.findAll());
        }

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario != null) {
                model.addAttribute("usuarioLogado", usuario);
            }
        }
        
        return "index"; 
    }

    @GetMapping("/produto/{id}")
    public String exibirDetalhes(@PathVariable Long id, Model model) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        if (produtoOptional.isPresent()) {
            model.addAttribute("produto", produtoOptional.get());
            return "detalhes";
        } else {
            return "redirect:/";
        }
    }
}