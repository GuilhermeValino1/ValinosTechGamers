package br.com.valinostech;

import br.com.valinostech.model.Usuario;
import br.com.valinostech.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ValinostechgamersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValinostechgamersApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            Usuario admin = usuarioRepository.findByEmail("guivalino@gmail.com");
            if (admin == null) {
                admin = new Usuario();
                admin.setEmail("guivalino@gmail.com");
                admin.setNome("Guilherme");
                admin.setTipo("ADMIN");
                admin.setCep("01001000");
                admin.setCidade("Sao Paulo");
                admin.setEstado("SP");
                admin.setCpf("12345678901");
                admin.setEndereco("Rua Exemplo");
                admin.setNumero("123");
                admin.setTelefone("11999999999");
            }
            admin.setSenha(passwordEncoder.encode("123456"));
            usuarioRepository.save(admin);
            System.out.println(">>> USUÁRIO ADMIN GARANTIDO NO BANCO COM SUCESSO! <<<");
        };
    }
}