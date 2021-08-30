package com.mcubes.stora_accounts.repository;

import com.mcubes.stora_accounts.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    @Query("select t from Transactions t where t.userId = ?1 and  (?2 is null or lower(t.description) like %?2% or lower(t.type) like %?2%)")
    Page<Transactions> findSearchAndPageableTransactionsByUserId(int userId, String search, Pageable pageable);

    Transactions findByIdAndUserId(long id, int userId);

    @Query("select t from Transactions t where t.userId = ?3 and (?1 is not null or t.date > ?1) and (?2 is not null or t.date < ?2)")
    List<Transactions> findByDateFilterAndUserId(String startDate, String endDate, int userId);

    List<Transactions> findByUserId(int userid);
}
