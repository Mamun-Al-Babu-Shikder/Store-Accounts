package com.mcubes.stora_accounts.util;

import com.mcubes.stora_accounts.entity.AppUser;
import com.mcubes.stora_accounts.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class Utils {

    @Autowired
    private AppUserRepository appUserRepository;

    public int loadUserInfo(Model model) {
        AppUser user = getLoginAppUser();
        model.addAttribute("username", user.getName());
        model.addAttribute("store_name", user.getStoreName());
        return user.getId();
    }

    public int getLoginUserId() {
        return getLoginAppUser().getId();
    }

    private AppUser getLoginAppUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return  appUserRepository.findByEmail(email);
    }


}
