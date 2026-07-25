package br.com.valinostech.services;

import br.com.valinostech.dto.CieloPagamentoRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class CieloService {

    @Autowired
    private AntifraudeService antifraudeService;

    @Value("${cielo.merchant.id:exemplo_id}")
    private String merchantId;

    @Value("${cielo.merchant.key:exemplo_key}")
    private String merchantKey;

    @Value("${cielo.environment:sandbox}")
    private String environment;

    private String cieloUrl;

    @PostConstruct
    public void init() {
        if ("producao".equalsIgnoreCase(environment)) {
            this.cieloUrl = "https://api.cieloecommerce.cielo.com.br/1/sales/";
            System.out.println("⚠️ ALERTA: VALINO'S TECH GAMES EM MODO REAL!");
        } else {
            this.cieloUrl = "https://apisandbox.cieloecommerce.cielo.com.br/1/sales/";
            System.out.println("🔄 CIELO CONFIGURADA EM MODO SANDBOX (TESTES).");
        }
    }

    public String processarPagamentoCartao(CieloPagamentoRequestDTO dados) {
        boolean transacaoSegura = antifraudeService.analisarTransacao(dados);
        if (!transacaoSegura) {
            return "RECUSADO_ANTIFRAUDE";
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("MerchantId", merchantId);
            headers.set("MerchantKey", merchantKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("MerchantOrderId", "VALINO-" + System.currentTimeMillis());

            Map<String, Object> customer = new HashMap<>();
            customer.put("Name", dados.getCreditCardHolderInfo().getName());
            requestBody.put("Customer", customer);

            Map<String, Object> payment = new HashMap<>();
            payment.put("Type", "CreditCard");
            payment.put("Amount", dados.getValue().multiply(new java.math.BigDecimal(100)).intValue());
            payment.put("Installments", 1);

            Map<String, Object> creditCard = new HashMap<>();
            creditCard.put("CardNumber", dados.getCreditCard().getNumber());
            creditCard.put("Holder", dados.getCreditCard().getHolderName());
            creditCard.put("ExpirationDate", dados.getCreditCard().getExpiryMonth() + "/" + dados.getCreditCard().getExpiryYear());
            creditCard.put("SecurityCode", dados.getCreditCard().getCcv());
            creditCard.put("Brand", "Visa");

            payment.put("CreditCard", creditCard);
            requestBody.put("Payment", payment);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Resolvido o Type Safety usando ParameterizedTypeReference
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                cieloUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<?, ?> paymentResponse = (Map<?, ?>) response.getBody().get("Payment");
                if (paymentResponse != null) {
                    Integer status = (Integer) paymentResponse.get("Status");
                    if (status != null && (status == 1 || status == 2)) {
                        return "APROVADO";
                    }
                }
            }
            return "RECUSADO_BANCO";

        } catch (Exception e) {
            return "ERRO_CONEXAO";
        }
    }

    public Map<String, String> gerarPixBancoDoBrasil(CieloPagamentoRequestDTO dados) {
        Map<String, String> pixResult = new HashMap<>();
        pixResult.put("status", "AGUARDANDO_PAGAMENTO");
        pixResult.put("copia_e_cola", "valinostech_pix_copia_e_cola_oficial");
        return pixResult;
    }

    public String gerarBoletoBancoDoBrasil(CieloPagamentoRequestDTO dados) {
        return "https://www.bb.com.br/boleto_valinostech_exemplo.pdf";
    }
}