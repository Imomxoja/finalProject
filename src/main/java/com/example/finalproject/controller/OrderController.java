package com.example.finalproject.controller;

import com.example.finalproject.domain.dto.request.OrderRequest;
import com.example.finalproject.domain.dto.response.BaseResponse;
import com.example.finalproject.domain.dto.response.OrderResponse;
import com.example.finalproject.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard/orders")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/add")
    public String updateEmployee(@ModelAttribute OrderRequest userRequest, Model model) {
        BaseResponse<OrderResponse> response = orderService.create(userRequest);
        model.addAttribute("response", response.getMessage());
        return "redirect:/dashboard/orders";
    }
    @GetMapping
    public String getList(Model model) {
        model.addAttribute("response", orderService.findAll());
        return "order";
    }

    @PostMapping("/delete")
    public String deleteWorker(@RequestParam("id") UUID id) {
        orderService.delete(id);
        return "redirect:/dashboard/orders";
    }
    @PostMapping("/update")
    public String updateUser(@RequestParam("id") UUID id, @ModelAttribute OrderRequest orderRequest) {
        orderService.update(orderRequest, id);
        return "redirect:/dashboard/orders";


        //    @PostMapping("/add")
//    public String updateEmployee(@ModelAttribute ProductRequest productRequest) {
//        orderService.create(productRequest);
//
//        return "redirect:/dashboard/products";
//    }

//    @PostMapping("/delete")
//    public String deleteWorker(@RequestParam("id") UUID id) {
//        productService.delete(id);
//        return "redirect:/dashboard/products";
//    }
//    @PostMapping("/update")
//    public String updateUser(@RequestParam("id") UUID id, @ModelAttribute ProductRequest productRequest) {
//        productService.update(productRequest, id);
//        return "redirect:/dashboard/products";
//    }
    }
}