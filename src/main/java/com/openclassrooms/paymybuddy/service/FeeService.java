package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.Fee;
import com.openclassrooms.paymybuddy.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeeService {

    @Autowired
    private FeeRepository feeRepository;

    public Iterable<Fee> getFees() {
        return feeRepository.findAll();
    }

    public Optional<Fee> getFeeById(Integer id) {
        return feeRepository.findById(id);
    }
}
