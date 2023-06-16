package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    Iterable<User> getUsers();

    Optional<User> getUserById(Integer id);

    User getCurrentUser();
}
