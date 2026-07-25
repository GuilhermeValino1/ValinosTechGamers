package br.com.valinostech.controller; // Ajustado para o singular "controller"

import br.com.valinostech.services.BancoDoBrasilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teste-bb")
public class PagamentoTesteController {

    @Autowired
    private BancoDoBrasilService bbService;

    @GetMapping("/token")
    public String testarToken() {
        try {
            // Aciona o serviço que salvamos antes
            String token = bbService.obterAccessToken();
            return "Sucesso! Token gerado pelo Banco do Brasil Sandbox: " + token;
        } catch (Exception e) {
            return "Erro ao gerar token: " + e.getMessage();
        }
    }
}