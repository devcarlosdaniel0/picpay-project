package com.projeto.picpay.security.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AuthDTO(@NotBlank String username, @NotBlank String password) {
}
