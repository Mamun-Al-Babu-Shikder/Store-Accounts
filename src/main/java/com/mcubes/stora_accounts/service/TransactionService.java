package com.mcubes.stora_accounts.service;

import com.mcubes.stora_accounts.entity.Product;
import com.mcubes.stora_accounts.entity.Transactions;
import com.mcubes.stora_accounts.model.DataTable;
import com.mcubes.stora_accounts.repository.ProductRepository;
import com.mcubes.stora_accounts.repository.TransactionsRepository;
import com.mcubes.stora_accounts.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private Utils utils;
    private ProductRepository productRepository;
    private TransactionsRepository transactionsRepository;

    public DataTable<Transactions> fetchPageableTransactions(int draw, String search, Pageable pageable) {
        DataTable<Transactions> transactionsDataTable = new DataTable<>();
        Page<Transactions> productPage = transactionsRepository.findSearchAndPageableTransactionsByUserId(
                utils.getLoginUserId(), search.trim().toLowerCase(), pageable);
        transactionsDataTable.setDraw(draw);
        transactionsDataTable.setData(productPage.getContent());
        transactionsDataTable.setRecordsFiltered(productPage.getTotalElements());
        transactionsDataTable.setRecordsTotal(productPage.getTotalElements());
        return transactionsDataTable;
    }

    public boolean saveTransactions(Transactions transactions) {
        boolean success = true;
        try {
            transactions.setDate(new Date());
            transactions.setProductId(0);
            transactions.setQuantity(0);
            transactions.setUserId(utils.getLoginUserId());
            transactionsRepository.save(transactions);
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    @Transactional
    public boolean deleteTransactions(long id) {
        boolean success = true;
        try {
            int userId = utils.getLoginUserId();
            Transactions transactions = transactionsRepository.findByIdAndUserId(id, userId);
            if (transactions.getProductId() > 0) {
                long productId = transactions.getProductId();
                Product product = productRepository.findByIdAndUserId(productId, userId);
                int currentStock = product.getStock();
                switch (transactions.getType()) {
                    case INCOME:
                        product.setStock(currentStock + transactions.getQuantity());
                        break;
                    case EXPENDITURE:
                        int newStock = currentStock - transactions.getQuantity();
                        if (newStock < 0) {
                            throw new RuntimeException("Stock not available");
                        }
                        product.setStock(newStock);
                        break;
                }
                productRepository.save(product);
            }
            transactionsRepository.delete(transactions);
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    public List<Transactions> getTransactionsChartData() {
        List<Transactions> transactions = Collections.emptyList();
        try {
            transactions = transactionsRepository.findByUserId(utils.getLoginUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
