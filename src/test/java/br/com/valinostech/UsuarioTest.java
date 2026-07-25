package br.com.valinostech;

import br.com.valinostech.model.Usuario;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UsuarioTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveValidarUsuarioCorreto() {
        Usuario usuario = new Usuario();
        usuario.setNome("Guilherme Rosa");
        usuario.setEmail("guilherme@valinostech.com");
        usuario.setSenha("senha123");
        usuario.setCpf("12345678901");
        usuario.setTelefone("11999999999");
        usuario.setCep("01001000");

        var violations = validator.validate(usuario);
        assertTrue(violations.isEmpty(), "O usuário deveria passar sem erros de validação");
    }

    @Test
    void naoDevePermitirCpfInvalido() {
        Usuario usuario = new Usuario();
        usuario.setNome("Guilherme Rosa");
        usuario.setEmail("guilherme@valinostech.com");
        usuario.setSenha("senha123");
        usuario.setCpf("123"); // CPF inválido
        usuario.setTelefone("11999999999");
        usuario.setCep("01001000");

        var violations = validator.validate(usuario);
        assertFalse(violations.isEmpty(), "Deveria barrar o CPF com menos de 11 dígitos");
    }
}