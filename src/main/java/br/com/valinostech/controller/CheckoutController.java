package br.com.valinostech.controller;

import br.com.valinostech.model.Pedido;
import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.PedidoRepository;
import br.com.valinostech.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrinho")
public class CheckoutController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/finalizar")
    public String finalizarCompra(@ModelAttribute("pagamentoForm") PagamentoForm form, 
                                  Authentication authentication, 
                                  HttpSession session) {
        
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName());

        Pedido novoPedido = new Pedido();
        novoPedido.setUsuario(usuario);
        
        Double total = (Double) session.getAttribute("totalGeral");
        novoPedido.setValorTotal(total != null ? total : 0.0);
        novoPedido.setStatus("AGUARDANDO PAGAMENTO");
        novoPedido.setMetodoPagamento(form.getMetodoPagamento());

        // Salva primeiro para gerar o ID no banco
        pedidoRepository.save(novoPedido);

        // Gera e associa o ID de transação para o Webhook
        String txid = "ECOM" + String.format("%020d", novoPedido.getId());
        novoPedido.setIdTransacaoGateway(txid);
        
        // Salva novamente com o txid
        pedidoRepository.save(novoPedido);

        session.removeAttribute("carrinho");
        session.removeAttribute("totalGeral");

        return "redirect:/carrinho/sucesso?id=" + novoPedido.getId();
    }

    @GetMapping("/sucesso")
    public String paginaSucesso() {
        return "sucesso";
    }
}