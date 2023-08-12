package com.example.finalproject.controller;

import com.example.finalproject.domain.dto.request.UserRequest;
import com.example.finalproject.domain.entity.user.UserEntity;
import com.example.finalproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test/employees")
public class TestController {
    private final UserService userService;

    @GetMapping("/get-one/{id}")
    @ResponseBody
    public Optional<UserEntity> showEditModal(@PathVariable("id") UUID id) {
        return userService.getOneUser(id);
    }

    //    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
    @PostMapping("/update")
    public String updateUser(@RequestParam("id") UUID id, @ModelAttribute UserRequest userRequest) {
        userService.update(userRequest, id);
        return "redirect:/dashboard/employees";
    }
}
