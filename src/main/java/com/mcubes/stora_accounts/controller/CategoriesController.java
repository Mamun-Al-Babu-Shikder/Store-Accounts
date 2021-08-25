package com.mcubes.stora_accounts.controller;

import com.mcubes.stora_accounts.entity.Category;
import com.mcubes.stora_accounts.model.DataTable;
import com.mcubes.stora_accounts.repository.CategoryRepository;
import com.mcubes.stora_accounts.service.CategoryService;
import com.mcubes.stora_accounts.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class CategoriesController {

    private Utils utils;
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categoriesPage(Model model) {
        int userId = utils.loadUserInfo(model);
        model.addAttribute("selected_option", "Categories");
        return "categories";
    }

    @ResponseBody
    @SuppressWarnings("deprecation")
    @PostMapping("/sava-category")
    public boolean saveOrUpdateCategory(@RequestParam(defaultValue = "0", required = false) int id,
                                        @RequestParam String name) {
        return categoryService.saveCategory(id, name);
    }

    @ResponseBody
    @Transactional
    @PostMapping("/delete-category")
    public boolean deleteCategory(@RequestParam long id) {
        return categoryService.deleteCategory(id);
    }

    @ResponseBody
    @PostMapping("/category-exist")
    public boolean isCategoryExist(@RequestParam String name) {
        return categoryService.isCategoryExists(name);
    }

    @ResponseBody
    @RequestMapping("/fetch-categories")
    public DataTable<Category> fetchData(@RequestParam int draw,
                               @RequestParam int start,
                               @RequestParam(name = "search[value]") String search,
                               @RequestParam(name = "order[0][column]") int order,
                               @RequestParam(name = "order[0][dir]") String dir,
                               @RequestParam int length) {
        String[] columns = "id,name,date,userId".split(",");
        Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest
                .of(start == 0 ? start : start / length, length, Sort.by(direction, columns[order]));
        DataTable<Category> dataTable = categoryService
                .fetchPageableCategories(draw, search.trim().toLowerCase(), pageable);
        return dataTable;
    }


}
