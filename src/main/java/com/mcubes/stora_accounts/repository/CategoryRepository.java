package com.mcubes.stora_accounts.repository;

import com.mcubes.stora_accounts.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.userId = ?1 and (?2 is null or lower(c.name) like ?2% or lower(c.date) like ?2%)")
    Page<Category> findSearchAndPageableCategoryByUserId(int userId, String search, Pageable pageable);

    List<Category> findByUserId(int userId);

    boolean existsByIdAndUserId(long id, int userId);

    boolean existsByNameAndUserId(String name, int userId);

    void deleteCategoryByIdAndUserId(long id, int userId);

}
