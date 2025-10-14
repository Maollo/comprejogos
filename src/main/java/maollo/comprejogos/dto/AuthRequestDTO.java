package maollo.comprejogos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {

    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "O formato do email é inválido.")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia.")
    private String password;
}