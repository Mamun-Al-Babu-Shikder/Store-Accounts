package com.mcubes.stora_accounts.controller;

import com.mcubes.stora_accounts.entity.Transactions;
import com.mcubes.stora_accounts.service.TransactionService;
import com.mcubes.stora_accounts.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class DataAnalysisController {

    private Utils utils;
    private TransactionService transactionService;

    @GetMapping("/transactions-chart")
    public String transactionsChartPage(Model model) {
        utils.loadUserInfo(model);
        model.addAttribute("selected_option", "Transactions-Chart");
        return "transactions_chart";
    }

    @ResponseBody
    @GetMapping("/fetch-transactions-chart-data")
    public List<Transactions> fetchTransactionsChartData(@RequestParam String startDate, @RequestParam String endDate,
                                                         @RequestParam String filterValue) {
        return transactionService.getTransactionsChartData(startDate, endDate, filterValue);
    }

}
