package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.Fee;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeeService {

    @Autowired
    private FeeRepository feeRepository;

    public List<Fee> getFeesByUser(User user) {
        return feeRepository.findByTransactionContactUser(user);
    }

    public Iterable<Fee> getFees() {
        return feeRepository.findAll();
    }

    public Optional<Fee> getFeeById(Integer id) {
        return feeRepository.findById(id);
    }
}
