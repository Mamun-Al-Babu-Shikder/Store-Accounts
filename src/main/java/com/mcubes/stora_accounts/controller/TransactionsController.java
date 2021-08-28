package com.mcubes.stora_accounts.controller;

import com.mcubes.stora_accounts.entity.Category;
import com.mcubes.stora_accounts.entity.Product;
import com.mcubes.stora_accounts.entity.Transactions;
import com.mcubes.stora_accounts.model.DataTable;
import com.mcubes.stora_accounts.service.TransactionService;
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
public class TransactionsController {

    private Utils utils;
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public String TransactionsPage(Model model) {
        int userId = utils.loadUserInfo(model);
        model.addAttribute("transactions_type", Transactions.Type.values());
        model.addAttribute("selected_option", "Transactions");
        return "transactions";
    }

    @ResponseBody
    @GetMapping("/fetch-transactions")
    public DataTable<Transactions> fetchProducts(@RequestParam int draw,
                                            @RequestParam int start,
                                            @RequestParam(name = "search[value]") String search,
                                            @RequestParam(name = "order[0][column]") int order,
                                            @RequestParam(name = "order[0][dir]") String dir,
                                            @RequestParam int length) {

        String[] columns = "id,description,type,type,amount,date".split(",");

        Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(start == 0 ? start : start / length, length, Sort.by(direction,
                columns[order]));
        DataTable<Transactions> transactionsDataTable = transactionService.fetchPageableTransactions(draw, search, pageable);
        return transactionsDataTable;
    }

    @ResponseBody
    @PostMapping("/save-transactions")
    public ResponseEntity<Map<String, Object>> saveTransaction(@Validated @ModelAttribute Transactions transactions,
                                                               BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            Map<String, String> validationError = new HashMap<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                if (fieldError.getField().equals("type")) {
                    validationError.put(fieldError.getField(), "Selected invalid type");
                } else {
                    validationError.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            }
            response.put("validationError", validationError);
        } else if (!transactionService.saveTransactions(transactions)) {
            response.put("error", true);
        }
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @PostMapping("delete-transactions")
    public boolean deleteTransaction(@RequestParam long id) {
        return transactionService.deleteTransactions(id);
    }

}
