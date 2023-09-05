package com.example.finalproject.controller;

import com.example.finalproject.domain.dto.request.ProductRequest;
import com.example.finalproject.domain.dto.response.BaseResponse;
import com.example.finalproject.domain.dto.response.CategoryResponse;
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
//dd
    @GetMapping("/get-one")
    @ResponseBody
    public Optional<ProductEntity> showEditModal(@RequestParam("id") UUID productId) {
        return productService.getOneProduct(productId);
    }
    @PostMapping("/add")
    public String updateEmployee(@ModelAttribute ProductRequest productRequest) {
        productService.create(productRequest);
        return "redirect:/dashboard/products";
    }
    @GetMapping
    public String getList(Model model, String keyword) {

        if (Objects.nonNull(keyword)) {
            model.addAttribute("response", productService.findByKeyword(keyword));
        } else {
            model.addAttribute("response", productService.findAll());
            model.addAttribute("categories",categoryService.findAllChildCategories().getData());
        }
        return "product";
    }
    @PostMapping("/delete")
    public String deleteWorker(@RequestParam("id") UUID id) {
        productService.delete(id);
        return "redirect:/dashboard/products";
    }
    @PostMapping("/update")
    public String updateUser(@RequestParam("id") UUID id, @ModelAttribute ProductRequest productRequest) {
        productService.update(productRequest, id);
        return "redirect:/dashboard/products";
    }

    @GetMapping("/get-categories")
    public String getAllCategories(Model model) {
        BaseResponse<List<CategoryResponse>> categories = categoryService.findAllChildCategories();
        model.addAttribute("categories", categories.getData());
        return "product";
    }

    public String errors(BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();

            StringBuilder sb = new StringBuilder();
            for (ObjectError allError : allErrors) {
                sb.append(allError.getDefaultMessage()).append("\n");
            }
            return sb.toString();
        }
        return null;
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
