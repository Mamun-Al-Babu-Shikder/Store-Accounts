package com.mcubes.stora_accounts.repository;

import com.mcubes.stora_accounts.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    AppUser findByEmail(String email);

}
