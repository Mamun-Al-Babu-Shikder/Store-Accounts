package com.mcubes.stora_accounts.repository;

import com.mcubes.stora_accounts.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByIdAndUserId(long id, int userId);

    @Query("select p from Product p where p.userId = ?1 and (?2 is null or lower(p.name) like %?2% or lower(p.category.name) like %?2%)")
    Page<Product> findSearchAndPageableProductByUserId(int userId, String search, Pageable pageable);

    void deleteProductByIdAndUserId(long id, int userId);

    @Query("select count(p) from Product p where p.category.id = ?1 and p.userId = ?2")
    long countProductUsingCategoryIdAndUserId(long category, int userId);

    @Query("select count(p) from Product p where lower(p.name) = ?1 and not p.id=?2 and p.userId = ?3")
    long countProductByNameIdAndUserId(String name, long id, int userId);

    @Modifying
    @Query("update Product p set p.stock =  (p.stock + ?2) where p.id = ?1 and p.userId = ?3")
    void updateProductStock(long id, int quantity, int userId);

    @Query("select p.stock from Product p where p.id = ?1 and p.userId = ?2")
    int getAvailableSock(long id, int userId);
}
