package com.example.finalproject.domain.dto.request;

import com.example.finalproject.domain.entity.user.Permission;
import com.example.finalproject.domain.entity.user.Role;
import com.example.finalproject.domain.entity.user.UserState;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @Pattern(regexp = "^[A-Za-z]+$",
            message = "name is not valid")
    private String name;

    @Pattern(regexp = "^[A-Za-z]+$",
            message = "username is not valid")
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)",
            message = "password is not valid")
    @Length(min = 8,
            max = 20,
            message = "must have between 8 and 20 characters")
    private String password;

    private Set<Role> roles;

    private Set<Permission> permissions;

    private String chatId;

    private UserState userState;

    private Double balance;

    public UserRequest(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public UserRequest(String name, String username, String password, Set<Role> roles, Set<Permission> permissions) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
    }
}
