package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/home")
    public String registrationForm(Model model) {
        try {
            User currentUser = userService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "home";
    }
}
