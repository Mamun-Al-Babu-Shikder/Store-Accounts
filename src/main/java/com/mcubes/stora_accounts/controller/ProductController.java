package com.mcubes.stora_accounts.controller;

import com.mcubes.stora_accounts.entity.Category;
import com.mcubes.stora_accounts.entity.Product;
import com.mcubes.stora_accounts.model.DataTable;
import com.mcubes.stora_accounts.repository.CategoryRepository;
import com.mcubes.stora_accounts.service.ProductService;
import com.mcubes.stora_accounts.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ProductController {

    private Utils utils;
    private CategoryRepository categoryRepository;
    private ProductService productService;

    @GetMapping("/products")
    public String productsPage(Model model) {
        int userId = utils.loadUserInfo(model);
        List<Category> categories = categoryRepository.findByUserId(userId);
        model.addAttribute("selected_option", "Products");
        model.addAttribute("categories", categories);
        return "products";
    }

    @ResponseBody
    @GetMapping("/fetch-product")
    public Product fetchProduct(@RequestParam long id) {
        return productService.getProductById(id);
    }

    /*
    @ResponseBody
    @GetMapping("/prodcut-exist")
    public boolean isProductExist(String name) {
        return productService.isProductExistByName(name);
    }

     */

    @ResponseBody
    @GetMapping("/fetch-products")
    public DataTable<Product> fetchProducts(@RequestParam int draw,
                                            @RequestParam int start,
                                            @RequestParam(name = "search[value]") String search,
                                            @RequestParam(name = "order[0][column]") int order,
                                            @RequestParam(name = "order[0][dir]") String dir,
                                            @RequestParam int length,
                                            @RequestParam String cat) {

        System.out.println("## " + cat);

        String[] columns = "id,name,price,stock,category,lastPurchased,lastSold".split(",");

        Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(start == 0 ? start : start / length, length, Sort.by(direction,
                columns[order]));
        DataTable<Product> productDataTable = productService.fetchPageableProducts(draw, search, pageable);
        return productDataTable;
    }

    @ResponseBody
    @PostMapping("/save-product")
    public ResponseEntity<Map<String, Object>> saveProduct(@Validated @ModelAttribute Product product, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            Map<String, String> validationError = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            response.put("validationError", validationError);
        } else if (productService.isProductExistByNameAndId(product.getName(), product.getId())) {
            response.put("productExist", true);
        } else if(!productService.saveProduct(product)) {
            response.put("error", true);
        }
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @PostMapping("/delete-product")
    public boolean deleteProduct(@RequestParam long id) {
        return productService.deleteProduct(id);
    }

}
