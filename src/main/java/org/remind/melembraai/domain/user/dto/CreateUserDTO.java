package org.remind.melembraai.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    private String password;
    @NotBlank(message = "Confirmação de senha é obrigatória")
    private String confirmPassword;
}
