package maollo.comprejogos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "O login não pode ser vazio.")
    @Size(min = 3, max = 50, message = "O login deve ter entre 3 e 50 caracteres.")
    private String login;

    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "O formato do email é inválido.")
    private String email;

    @NotBlank(message = "A senha не pode ser vazia.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    private String password;
}