package com.mcubes.stora_accounts.controller;

import com.mcubes.stora_accounts.entity.AppUser;
import com.mcubes.stora_accounts.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class DashboardController {

    private Utils utils;


    @RequestMapping(value = {"/", "dashboard"})
    public String dashboard(Model model) {
        utils.loadUserInfo(model);
        model.addAttribute("selected_option", "dashboard");
        return "index";
    }



}
