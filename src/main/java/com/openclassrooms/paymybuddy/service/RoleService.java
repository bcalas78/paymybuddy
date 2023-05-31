package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.Role;
import com.openclassrooms.paymybuddy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Iterable<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Integer id) {
        return roleRepository.findById(id);
    }
}
