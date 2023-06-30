package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    Page<Transaction> findAll(Pageable pageable);

    List<Transaction> findAll();

    Transaction save(Transaction transaction);

    List<Transaction> findByContactUser(User user);
}
