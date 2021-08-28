package com.mcubes.stora_accounts.service;

import com.mcubes.stora_accounts.entity.Product;
import com.mcubes.stora_accounts.entity.Transactions;
import com.mcubes.stora_accounts.model.DataTable;
import com.mcubes.stora_accounts.repository.CategoryRepository;
import com.mcubes.stora_accounts.repository.ProductRepository;
import com.mcubes.stora_accounts.repository.TransactionsRepository;
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
    private TransactionsRepository transactionsRepository;

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
            Date date = new Date();
            product.setLastPurchased(product.getStock() > 0 ? date : null);
            productRepository.save(product);
            if (product.getStock() > 0) {
                Transactions transactions = new Transactions();
                transactions.setDescription(product.getName() + " ( " + product.getStock()+ " )");
                transactions.setProductId(product.getId());
                transactions.setQuantity(product.getStock());
                transactions.setAmount(product.getPrice() * product.getStock());
                transactions.setType(Transactions.Type.EXPENDITURE);
                transactions.setDate(date);
                transactions.setUserId(userId);
                transactionsRepository.save(transactions);
            }
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

    @Transactional
    public boolean updateProductStockForImport(long id, int quantity) {
        boolean success = true;
        try {
           // productRepository.updateProductStock(id, quantity, utils.getLoginUserId());
            int userId = utils.getLoginUserId();
            Date date = new Date();
            Product product = productRepository.findByIdAndUserId(id, userId);
            product.setStock(product.getStock() + quantity);
            product.setLastPurchased(date);
            
            Transactions transactions = new Transactions();
            transactions.setDescription(product.getName() + " (" + quantity + ")");
            transactions.setProductId(product.getId());
            transactions.setQuantity(quantity);
            transactions.setAmount(product.getPrice() * quantity);
            transactions.setDate(date);
            transactions.setType(Transactions.Type.EXPENDITURE);
            transactions.setUserId(userId);

            productRepository.save(product);
            transactionsRepository.save(transactions);

        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    @Transactional
    public boolean updateProductStockForSell(long id, int quantity, float totalCostForSell) {
        boolean success = true;
        try {
            int userId = utils.getLoginUserId();
            Date date = new Date();
            Product product = productRepository.findByIdAndUserId(id, userId);
            product.setStock(product.getStock() - quantity);
            product.setLastSold(date);

            Transactions transactions = new Transactions();
            transactions.setDescription(product.getName() + " (" + quantity + ")");
            transactions.setProductId(product.getId());
            transactions.setQuantity(quantity);
            transactions.setAmount(totalCostForSell);
            transactions.setDate(date);
            transactions.setType(Transactions.Type.INCOME);
            transactions.setUserId(userId);

            productRepository.save(product);
            transactionsRepository.save(transactions);

        } catch (Exception e){
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    public int getAvailableStock(long id) {
        return productRepository.getAvailableSock(id, utils.getLoginUserId());
    }
}
