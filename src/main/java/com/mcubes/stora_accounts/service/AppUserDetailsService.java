package com.mcubes.stora_accounts.service;

import com.mcubes.stora_accounts.details.AppUserDetails;
import com.mcubes.stora_accounts.entity.AppUser;
import com.mcubes.stora_accounts.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser == null)
            throw new UsernameNotFoundException("App user not found by email : " + email);
        return new AppUserDetails(appUser);
    }
}
