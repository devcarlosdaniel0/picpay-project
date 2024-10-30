package com.projeto.picpay.security.dto;

import com.projeto.picpay.security.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignupDTO (@NotBlank String username, @NotBlank String password, @NotNull UserRole role) {
}
