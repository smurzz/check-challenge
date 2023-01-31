package com.example.checkchallenge.controller.request;

import com.example.checkchallenge.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
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

    private boolean active;
    private List<UserRole> roles;

    public UserRequest(String firstName, String lastName, String position, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.email = email;
        this.password = password;
        setActive(true);
        setRoles(List.of(UserRole.USER));
    }


}
