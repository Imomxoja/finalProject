package com.example.finalproject.controller;

import com.example.finalproject.domain.dto.request.CategoryRequest;
import com.example.finalproject.domain.dto.response.BaseResponse;
import com.example.finalproject.domain.dto.response.CategoryResponse;
import com.example.finalproject.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String getList(Model model, String keyword) {
        if (Objects.nonNull(keyword)) {
            model.addAttribute("categories", categoryService.findByKeyword(keyword).getData());
        } else {
            List<CategoryResponse> all = categoryService.getParentCategories();
            model.addAttribute("categories",all);
        }
        return "categories";
    }
    @PostMapping("/add")
    public ModelAndView addCategory(@ModelAttribute CategoryRequest categoryRequest, Model model) {
        ModelAndView view = new ModelAndView("categories");
        BaseResponse<CategoryResponse> response = categoryService.create(categoryRequest);
        view.addObject("message", response.getMessage());
        view.addObject("categories", categoryService.findAll().getData());
        return view;
    }
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("id") UUID id) {
        categoryService.delete(id);
        return "redirect:/dashboard/categories";
    }
    @PostMapping("/update")
    public ModelAndView updateCategory(@RequestParam("id") UUID id, @ModelAttribute CategoryRequest categoryRequest) {
        ModelAndView view = new ModelAndView("categories");

        BaseResponse<CategoryResponse> update = categoryService.update(categoryRequest, id);
        view.addObject("message", update.getMessage());
        view.addObject("categories", categoryService.getParentCategories());
        return view;
    }

    @GetMapping("/children/{categoryId}")
    public String categoryChild(@PathVariable("categoryId") UUID categoryId, Model model, String keyword) {
        if (Objects.nonNull(keyword)) {
            model.addAttribute("categories", categoryService.findByKeywordForChild(keyword, categoryId).getData());
            model.addAttribute("parentId",categoryId);
        } else {
            List<CategoryResponse> all = categoryService.getChildCategories(categoryId);
            model.addAttribute("categories",all);
            model.addAttribute("parentId",categoryId);
        }

        return "category-child";
    }

    @PostMapping("/children/add")
    public ModelAndView addChildCategory(@ModelAttribute CategoryRequest categoryRequest, Model model) {
        ModelAndView view = new ModelAndView("category-child");
        BaseResponse<CategoryResponse> response = categoryService.create(categoryRequest);
        view.addObject("message", response.getMessage());
        view.addObject("parentId", response.getData().getParent().getId());
        view.addObject("categories", categoryService.getChildCategoriesWithChildId(response.getData().getId()).getData());
        return view;
    }
    @PostMapping("/children/update")
    public ModelAndView updateChildCategory(@RequestParam("id") UUID id, @ModelAttribute CategoryRequest categoryRequest) {
        ModelAndView view = new ModelAndView("category-child");
        BaseResponse<CategoryResponse> update = categoryService.update(categoryRequest, id);
        view.addObject("message", update.getMessage());
        view.addObject("categories", categoryService.getChildCategoriesWithChildId(id).getData());
        return view;
    }
    @PostMapping("/children/delete")
    public String deleteChildCategory(@RequestParam("id") UUID id, @RequestParam("parent") UUID parentId) {
        categoryService.delete(id);
        return "redirect:/dashboard/categories/children/" + parentId;
    }
}
