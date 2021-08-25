package com.mcubes.stora_accounts.service;

import com.mcubes.stora_accounts.entity.Category;
import com.mcubes.stora_accounts.model.DataTable;
import com.mcubes.stora_accounts.repository.CategoryRepository;
import com.mcubes.stora_accounts.repository.ProductRepository;
import com.mcubes.stora_accounts.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class CategoryService {

    private Utils utils;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public DataTable<Category> fetchPageableCategories(int draw, String search, Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findSearchAndPageableCategoryByUserId(utils.getLoginUserId(),
                search.trim().toLowerCase(), pageable);
        DataTable<Category> dataTable = new DataTable<>();
        dataTable.setDraw(draw);
        dataTable.setRecordsTotal(categoryPage.getTotalElements());
        dataTable.setRecordsFiltered(categoryPage.getTotalElements());
        dataTable.setData(categoryPage.getContent());
        return dataTable;
    }

    public boolean saveCategory(long id, String name) {
        boolean success = true;
        try {
            Category category = new Category();
            if (id != 0) {
                category.setId(id);
            }
            category.setName(name);
            category.setDate(new Date());
            category.setUserId(utils.getLoginUserId());
            categoryRepository.save(category);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean isCategoryExists(String name) {
        return categoryRepository.existsByNameAndUserId(name, utils.getLoginUserId());
    }

    public boolean deleteCategory(long id) {
        boolean success = false;
        try {
            if (productRepository.countProductUsingCategoryIdAndUserId(id, utils.getLoginUserId()) == 0) {
                categoryRepository.deleteCategoryByIdAndUserId(id, utils.getLoginUserId());
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

}
