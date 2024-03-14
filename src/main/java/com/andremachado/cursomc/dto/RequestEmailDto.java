package com.andremachado.cursomc.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class RequestEmailDto {

    @NotBlank(message="Email deve ser preenchido")
    @Email(message="Email inválido")
    private String email;

    public RequestEmailDto() {}

    public RequestEmailDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
