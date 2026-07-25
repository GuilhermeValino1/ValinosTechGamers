package br.com.valinostech.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServices {

    @Autowired
    private JavaMailSender mailSender;

    // 🚀 ADICIONADO: Puxa o e-mail oficial configurado no seu application.properties automaticamente
    @Value("${spring.mail.username}")
    private String emailRemetente;

    // Método para confirmação de compras na Valino's Tech Games
    public void enviarConfirmacaoPagamento(String emailCliente, String nomeCliente, String totalPedido) {
        SimpleMailMessage email = new SimpleMailMessage();
        
        email.setFrom(emailRemetente); // 🚀 Define o remetente oficial da loja
        email.setTo(emailCliente);
        email.setSubject("🔥 PAGAMENTO APROVADO! | Valino's Tech Games");
        email.setText("Olá, " + nomeCliente + "!\n\n" +
                      "Confirmamos o recebimento do seu pagamento.\n" +
                      "O seu setup de alta performance já entrou na nossa linha de montagem e calibração.\n\n" +
                      "Valor Total Processado: " + totalPedido + "\n\n" +
                      "Assim que os componentes forem despachados, você receberá o código de rastreamento.\n\n" +
                      "Obrigado por escolher a Valino's Tech Games! 🎮🚀");
        
        mailSender.send(email);
    }

    // Método para o Esqueci a Senha
    public void enviarLinkRecuperacao(String emailCliente, String token) {
        SimpleMailMessage email = new SimpleMailMessage();
        
        email.setFrom(emailRemetente); // 🚀 Define o remetente oficial da loja
        email.setTo(emailCliente);
        email.setSubject("🔑 RECUPERAÇÃO DE SENHA | Valino's Tech Games");
        
        // 🛠️ CORREÇÃO: Atualizado de 8081 para 8080 para bater com o seu server.port do application.properties
        String link = "http://localhost:8080/redefinir-senha?token=" + token;
        
        email.setText("Olá!\n\n" +
                      "Você solicitou a redefinição de senha para a sua conta na Valino's Tech Games.\n" +
                      "Clique no link abaixo para criar uma nova senha:\n\n" +
                      link + "\n\n" +
                      "Se você não solicitou essa mudança, ignore este e-mail.\n" +
                      "O link expira em breve por motivos de segurança.\n\n" +
                      "Suporte Valino's Tech Games 🎮");
                      
        mailSender.send(email);
    }
}