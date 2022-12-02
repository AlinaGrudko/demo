package com.example.demo.dto.password;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordDto {
    @NotBlank(message = "*Пожалуйста, введите ваш пароль")
    private String oldPassword;

    @Length(min = 5, message = "*Ваш пароль должен содержить минимум 5 символов")
    @NotBlank(message = "*Пожалуйста, укажите новый пароль")
    private String newPassword;
    private String matchingPassword;
}
