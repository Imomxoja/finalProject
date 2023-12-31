package com.example.finalproject.controller;

import com.example.finalproject.domain.dto.request.UserRequest;
import com.example.finalproject.domain.dto.response.BaseResponse;
import com.example.finalproject.domain.dto.response.UserResponse;
import com.example.finalproject.domain.entity.user.UserEntity;
import com.example.finalproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard/employees")
public class EmployeeController {
    private final UserService userService;

    @PostMapping("/add")
    public ModelAndView updateEmployee(@ModelAttribute UserRequest userRequest, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView view = new ModelAndView("employees");
        BaseResponse<UserResponse> response = userService.create(userRequest);
        view.addObject("message", response.getMessage());
        view.addObject("response", userService.findAll(authentication.getName()));
        return view;
    }

    @GetMapping
    public String getList(Model model, String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(keyword)) {
            model.addAttribute("response", userService.findByKeyword(keyword, authentication.getName()));
        } else {
            model.addAttribute("response", userService.findAll(authentication.getName()));
        }
        return "employees";
    }

    @PostMapping("/delete")
    public String deleteWorker(@RequestParam("id") UUID id) {
        userService.delete(id);
        return "redirect:/dashboard/employees";
    }

    @GetMapping("/get-one")
    @ResponseBody
    public Optional<UserEntity> showEditModal(@RequestParam("id") UUID id) {
        return userService.getOneUser(id);
    }

    @PostMapping("/update")
    public ModelAndView updateUser(@RequestParam("id") UUID id, @ModelAttribute UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView view = new ModelAndView("employees");
        BaseResponse<UserResponse> update = userService.update(userRequest, id);
        view.addObject("message", update.getMessage());
        view.addObject("response", userService.findAll(authentication.getName()));
        return view;
    }
}
