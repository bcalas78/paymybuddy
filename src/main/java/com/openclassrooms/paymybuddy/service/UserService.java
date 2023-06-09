package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.model.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    User getCurrentUser();
}
