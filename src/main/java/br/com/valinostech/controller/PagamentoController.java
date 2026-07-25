package br.com.valinostech.controller;

import br.com.valinostech.model.Pedido;
import br.com.valinostech.repository.PedidoRepository;
import br.com.valinostech.services.BancoDoBrasilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private BancoDoBrasilService bbService;
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping("/iniciar/{pedidoId}")
    public String iniciarPagamento(@PathVariable Long pedidoId, Model model, RedirectAttributes ra) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

        if (pedido == null) {
            ra.addFlashAttribute("mensagemErro", "Pedido não encontrado!");
            return "redirect:/admin/dashboard";
        }

        try {
            Map<String, Object> resposta = bbService.realizarCobrancaPix(pedido);
            String copiaECola = (String) resposta.get("pixCopiaECola");
            
            pedido.setStatus("AGUARDANDO PAGAMENTO");
            pedidoRepository.save(pedido);
            
            model.addAttribute("pedido", pedido);
            model.addAttribute("copiaECola", copiaECola);
            
            return "pagamento/pix";
            
        } catch (Exception e) {
            ra.addFlashAttribute("mensagemErro", "Erro ao processar pagamento: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }
}