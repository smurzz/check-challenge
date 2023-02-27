package com.example.checkchallenge.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.mongodb.core.index.Indexed;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;

    private String position;

    @NotBlank
    @Size(max = 40)
    @Indexed(unique = true)
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 20)
    private String password;
}
