package br.com.valinostech.services;

import br.com.valinostech.model.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.Map;
import java.util.HashMap;

@Service
public class BancoDoBrasilService {

    @Value("${bb.auth.url}") private String authUrl;
    @Value("${bb.pix.url}") private String pixUrl;
    @Value("${bb.client.id}") private String clientId;
    @Value("${bb.client.secret}") private String clientSecret;
    @Value("${bb.developer.application.key}") private String devKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String obterAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(clientId, clientSecret);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");
            body.add("scope", "pix.request pix.read");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                authUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação com o Banco do Brasil: " + e.getMessage());
        }
    }

    public Map<String, Object> realizarCobrancaPix(Pedido pedido) {
        String token = obterAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        headers.set("Authorization-Developer-Application-Key", devKey);

        Map<String, Object> body = new HashMap<>();
        body.put("valor", Map.of("original", String.format("%.2f", pedido.getValorTotal())));
        body.put("calendario", Map.of("expiracao", 3600));
        body.put("solicitacaoPagador", "Pedido #" + pedido.getId());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        String txid = String.format("ECOM%022d", pedido.getId()); 

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                pixUrl + "/cob/" + txid, HttpMethod.PUT, request, 
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("pixCopiaECola")) {
                throw new RuntimeException("API do BB não retornou o código Pix.");
            }
            return responseBody;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar cobrança Pix: " + e.getMessage());
        }
    }
}