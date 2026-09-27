package com.example.eCommBackendNew2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.aspectj.weaver.ast.Not;

public record RegisterUserRequest(

        @NotBlank
        @Size(min=3, max=50)
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min=6)
        String password
){
}
