package maollo.comprejogos.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateProfileDTO {

    @Size(max = 500, message = "URL do avatar deve ter no máximo 500 caracteres.")
    private String avatarUrl;

    @Past(message = "A data de nascimento deve ser uma data no passado.")
    private LocalDate birthDate;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres.")
    private String phone;

    @Size(max = 100, message = "País deve ter no máximo 100 caracteres.")
    private String country;

    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres.")
    private String city;
}