package br.com.valinostech.controller;

import br.com.valinostech.model.Pedido;
import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.PedidoRepository;
import br.com.valinostech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/meus-pedidos")
    public String listarMeusPedidos(Authentication authentication, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName());
        model.addAttribute("pedidos", pedidoRepository.findByUsuario(usuario));
        return "meus-pedidos";
    }

    @GetMapping("/pedido/detalhes/{id}")
    public String verDetalhes(@PathVariable Long id, Authentication authentication, Model model) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName());

        if (pedido == null || !pedido.getUsuario().getId().equals(usuarioLogado.getId())) {
            return "redirect:/meus-pedidos?erro=acesso-negado";
        }

        model.addAttribute("pedido", pedido);
        return "detalhes-pedido";
    }
}