package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Fee;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends CrudRepository<Fee, Integer> {

    Fee save(Fee fee);

    List<Fee> findByTransactionContactUser(User user);
}
