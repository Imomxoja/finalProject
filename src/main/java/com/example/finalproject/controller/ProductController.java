package com.example.finalproject.controller;

import com.example.finalproject.domain.dto.request.ProductRequest;
import com.example.finalproject.domain.dto.response.BaseResponse;
import com.example.finalproject.domain.dto.response.CategoryResponse;
import com.example.finalproject.domain.dto.response.ProductResponse;
import com.example.finalproject.domain.entity.product.ProductEntity;
import com.example.finalproject.service.category.CategoryService;
import com.example.finalproject.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor

@RequestMapping("/dashboard/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/get-one")
    @ResponseBody
    public Optional<ProductEntity> showEditModal(@RequestParam("id") UUID productId) {
        return productService.getOneProduct(productId);
    }

    @PostMapping("/add")
    public ModelAndView add(@ModelAttribute ProductRequest productRequest, Model model, BindingResult result) {
        ModelAndView view = new ModelAndView("product");

        BaseResponse<ProductResponse> response = productService.create(productRequest);
        view.addObject("message", response.getMessage());
        view.addObject("response", productService.findAll());

        return view;
    }

    @GetMapping
    public String getList(Model model, String keyword) {

        if (Objects.nonNull(keyword)) {
            model.addAttribute("response", productService.findByKeyword(keyword));
        } else {
            model.addAttribute("response", productService.findAll());
            model.addAttribute("categories", categoryService.findAllChildCategories().getData());
        }
        return "product";
    }

    @PostMapping("/delete")
    public String deleteWorker(@RequestParam("id") UUID id) {
        productService.delete(id);
        return "redirect:/dashboard/products";
    }

    @PostMapping("/update")
    public ModelAndView updateUser(@RequestParam("id") UUID id, @ModelAttribute ProductRequest productRequest) {

        ModelAndView view = new ModelAndView("product");
        BaseResponse<ProductResponse> update = productService.update(productRequest, id);
        view.addObject("message", update.getMessage());
        view.addObject("response", productService.findAll());
        return view;
    }

    @GetMapping("/get-categories")
    public String getAllCategories(Model model) {
        BaseResponse<List<CategoryResponse>> categories = categoryService.findAllChildCategories();
        model.addAttribute("categories", categories.getData());
        return "product";
    }

    @GetMapping("/all")
    public ModelAndView findByPage(
            ModelAndView view,
            @RequestParam Integer page
    ) {
        view.addObject("products", productService.findByPage(Optional.of(page), Optional.of(10)));
        return view;
    }
}
