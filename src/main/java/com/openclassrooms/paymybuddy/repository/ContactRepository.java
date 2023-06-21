package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {
    Optional<Contact> findByUserAndBuddy(User user, User buddy);

    List<Contact> findByUser(User user);

    boolean existsByUserAndBuddy(User user, User buddy);

    Contact save(Contact contact);
}
