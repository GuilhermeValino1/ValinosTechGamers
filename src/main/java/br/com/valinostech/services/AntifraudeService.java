package br.com.valinostech.services;

import br.com.valinostech.dto.CieloPagamentoRequestDTO;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class AntifraudeService {

    public boolean analisarTransacao(CieloPagamentoRequestDTO dados) {
        if (dados == null || dados.getCreditCard() == null || dados.getCreditCardHolderInfo() == null) {
            System.out.println("🚨 BLOQUEADO: Estrutura de dados de pagamento nula.");
            return false;
        }

        String nomeCartao = dados.getCreditCard().getHolderName() != null ? dados.getCreditCard().getHolderName().toUpperCase().trim() : "";
        String nomeTitularFisico = dados.getCreditCardHolderInfo().getName() != null ? dados.getCreditCardHolderInfo().getName().toUpperCase().trim() : "";
        String cpfRaw = dados.getCreditCardHolderInfo().getCpfCnpj();
        String cpf = cpfRaw != null ? cpfRaw.replaceAll("[^0-9]", "") : "";
        BigDecimal valor = dados.getValue() != null ? dados.getValue() : BigDecimal.ZERO;

        System.out.println("🛡️ Antifraude ativo para o CPF/CNPJ: " + cpf);

        String[] partesNomeCartao = nomeCartao.split("\\s+");
        String[] partesNomeTitular = nomeTitularFisico.split("\\s+");

        if (partesNomeCartao.length == 0 || partesNomeTitular.length == 0 || !partesNomeCartao[0].equals(partesNomeTitular[0])) {
            System.out.println("🚨 BLOQUEADO: Primeiro nome do cartão não bate com o comprador.");
            return false;
        }

        if (cpf.length() != 11 && cpf.length() != 14) {
            System.out.println("🚨 BLOQUEADO: CPF/CNPJ com tamanho inválido.");
            return false;
        }

        if (valor.compareTo(new BigDecimal("5000.00")) > 0) {
            System.out.println("⚠️ RETIDO: Valor acima de R$ 5.000,00 precisa de validação manual.");
            return false;
        }

        System.out.println("✅ OK: Transação considerada segura.");
        return true;
    }
}