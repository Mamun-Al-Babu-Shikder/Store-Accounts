package com.mcubes.stora_accounts.service;

import com.mcubes.stora_accounts.entity.Product;
import com.mcubes.stora_accounts.model.DataTable;
import com.mcubes.stora_accounts.repository.CategoryRepository;
import com.mcubes.stora_accounts.repository.ProductRepository;
import com.mcubes.stora_accounts.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
public class ProductService {

    private Utils utils;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public Product getProductById(long id) {
       Product product = productRepository.findByIdAndUserId(id, utils.getLoginUserId());
       return product;
    }

    public boolean isProductExistByNameAndId(String name, long id) {
        return productRepository.countProductByNameIdAndUserId(name.trim().toLowerCase(), id, utils.getLoginUserId()) != 0;
    }

    public DataTable<Product> fetchPageableProducts(int draw, String search, Pageable pageable) {
        DataTable<Product> productDataTable = new DataTable<>();
        Page<Product> productPage = productRepository.findSearchAndPageableProductByUserId(utils.getLoginUserId(),
                search.trim().toLowerCase(), pageable);
        productDataTable.setDraw(draw);
        productDataTable.setData(productPage.getContent());
        productDataTable.setRecordsFiltered(productPage.getTotalElements());
        productDataTable.setRecordsTotal(productPage.getTotalElements());
        return productDataTable;
    }

    public boolean saveProduct(Product product) {
        boolean success = false;
        long id = product.getId();
        int userId = utils.getLoginUserId();
        if (id == 0) {
            product.setUserId(userId);
            product.setName(product.getName().trim());
            product.setLastPurchased(product.getStock() > 0 ? new Date() : null);
            productRepository.save(product);
            success = true;
        } else {
            Product existingProduct = productRepository.findByIdAndUserId(id, userId);
            if (existingProduct != null) {
                existingProduct.setName(product.getName().trim());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setCategory(product.getCategory());
                productRepository.save(existingProduct);
                success = true;
            }
        }
        return success;
    }

    @Transactional
    public boolean deleteProduct(long id) {
        boolean success = true;
        try {
            productRepository.deleteProductByIdAndUserId(id, utils.getLoginUserId());
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

}
