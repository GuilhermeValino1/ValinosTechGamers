package br.com.valinostech.controller;

import br.com.valinostech.model.Pedido;
import br.com.valinostech.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping("/bb")
    @SuppressWarnings("unchecked")
    public ResponseEntity<String> receberNotificacao(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Extração segura da lista de pix
            List<Map<String, Object>> pixList = (List<Map<String, Object>>) payload.get("pix");
            
            if (pixList == null || pixList.isEmpty()) {
                return ResponseEntity.badRequest().body("Payload inválido: lista pix vazia.");
            }

            Map<String, Object> pixData = pixList.get(0);
            String txid = (String) pixData.get("txid");

            if (txid != null && txid.startsWith("ECOM")) {
                Long pedidoId = Long.parseLong(txid.replace("ECOM", ""));
                
                Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
                
                if (pedido != null) {
                    pedido.setStatus("PAGO");
                    pedidoRepository.save(pedido);
                    System.out.println("Pedido " + pedidoId + " atualizado para PAGO via Webhook.");
                    return ResponseEntity.ok("Status atualizado com sucesso.");
                }
            }
            
            return ResponseEntity.notFound().build(); // Pedido não encontrado
            
        } catch (Exception e) {
            System.err.println("Erro ao processar Webhook: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Erro interno ao processar.");
        }
    }
}