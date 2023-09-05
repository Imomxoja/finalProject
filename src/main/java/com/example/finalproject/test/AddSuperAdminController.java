package com.example.finalproject.test;

import com.example.finalproject.domain.dto.request.UserRequest;
import com.example.finalproject.domain.dto.response.BaseResponse;
import com.example.finalproject.domain.dto.response.UserResponse;
import com.example.finalproject.domain.entity.user.Permission;
import com.example.finalproject.domain.entity.user.Role;
import com.example.finalproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Set;
import java.util.function.Consumer;

@RestController
@RequestMapping("/auth/v1/api")
@RequiredArgsConstructor
public class AddSuperAdminController {
    private final UserService userService;
    @GetMapping("/super-admin")
    public BaseResponse<UserResponse> addSuperAdmin() {
        Set<Permission> perm = Set.of(Permission.ADD, Permission.EDIT, Permission.GET, Permission.DELETE);

        return userService.create(
                new UserRequest("Khamroz", "admin", "1", Set.of(Role.ADMIN, Role.SUPER_ADMIN), perm));
    }
}
