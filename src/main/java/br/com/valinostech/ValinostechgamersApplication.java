package br.com.valinostech;

import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ValinostechgamersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValinostechgamersApplication.class, args);
    }

    @Bean
    public CommandLineRunner criarUsuariosIniciais(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Cria o Administrador
            String emailAdmin = "guivalino@gmail.com";
            if (usuarioRepository.findByEmail(emailAdmin) == null) {
                Usuario admin = new Usuario();
                admin.setNome("Guilherme Rabeca Valino Rosa");
                admin.setCpf("43107623873"); // 11 dígitos corretos sem pontos/traço se exigido
                admin.setEmail(emailAdmin);
                admin.setSenha(passwordEncoder.encode("Guivalino2019@"));
                admin.setTelefone("11999999999");
                admin.setCep("05128190");
                admin.setTipo("ADMIN");
                
                usuarioRepository.save(admin);
                System.out.println(">>> USUÁRIO ADMIN CRIADO COM SUCESSO!");
            }

            // 2. Cria o Cliente de Teste
            String emailCliente = "cliente@valinos.com";
            if (usuarioRepository.findByEmail(emailCliente) == null) {
                Usuario cliente = new Usuario();
                cliente.setNome("Cliente Teste");
                cliente.setCpf("12345678900");
                cliente.setEmail(emailCliente);
                cliente.setSenha(passwordEncoder.encode("123456"));
                cliente.setTelefone("11888888888");
                cliente.setCep("05128190");
                cliente.setTipo("CLIENTE");
                
                usuarioRepository.save(cliente);
                System.out.println(">>> CLIENTE DE TESTE CRIADO COM SUCESSO!");
            }
        };
    }
}