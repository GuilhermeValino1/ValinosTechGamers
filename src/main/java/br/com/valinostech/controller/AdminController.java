package br.com.valinostech.controller;

import br.com.valinostech.model.Produto;
import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.UsuarioRepository;
import br.com.valinostech.repository.ProdutoRepository;
import br.com.valinostech.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AdminController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private PedidoRepository pedidoRepository;

    // --- DASHBOARD E PEDIDOS ---
    @GetMapping("/admin/dashboard")
    public String exibirDashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalProdutos", produtoRepository.count());
        
        Double faturamento = pedidoRepository.calcularFaturamentoTotal();
        model.addAttribute("faturamentoTotal", faturamento != null ? faturamento : 0.0);
        model.addAttribute("pedidos", pedidoRepository.findAll());
        return "admin/dashboard"; 
    }

    // --- USUÁRIOS ---
    @GetMapping("/admin/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin/usuarios"; 
    }

    @PostMapping("/admin/usuarios/promover/{id}")
    @Transactional
    public String promoverParaAdmin(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setTipo("ADMIN");
            usuarioRepository.save(usuario);
        }
        return "redirect:/admin/usuarios?sucesso=true";
    }

    // --- PRODUTOS ---
    @GetMapping("/admin/produtos")
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        return "admin/lista_produtos";
    }

    @GetMapping("/admin/produtos/novo")
    public String formularioNovoProduto(Model model) {
        model.addAttribute("produto", new Produto());
        return "admin/cadastro_produto";
    }

    @PostMapping("/admin/produtos/salvar")
    @Transactional
    public String salvarProduto(@ModelAttribute("produto") Produto produto, 
                                @RequestParam("fileImagem") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String pastaDestino = "src/main/resources/static/img/";
                String nomeArquivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(pastaDestino + nomeArquivo);
                Files.write(path, file.getBytes());
                produto.setImagemUrl("/img/" + nomeArquivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        produtoRepository.save(produto);
        return "redirect:/admin/produtos";
    }

    @GetMapping("/admin/produtos/deletar/{id}")
    @Transactional
    public String deletarProduto(@PathVariable Long id) {
        produtoRepository.deleteById(id);
        return "redirect:/admin/produtos";
    }
}