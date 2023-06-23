package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.BankAccountDto;
import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.Fee;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.service.FeeService;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    FeeService feeService;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    BankAccountService bankAccountService;

    @GetMapping("/home")
    public String registrationForm(Model model) {

        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "home";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        String email = principal.getName();
        User currentUser = userService.findUserByEmail(email);

        List<Fee> fees = feeService.getFeesByUser(currentUser);
        model.addAttribute("fees", fees);

        BankAccount bankAccount = currentUser.getBankAccount();
        if(bankAccount == null) {
            bankAccount = new BankAccount();
            bankAccount.setUser(currentUser);
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("bankAccount", bankAccount);
        return "profile-page";
    }

    @PostMapping("/profile")
    public String saveBankAccount(@ModelAttribute("bank_account") BankAccount bankAccount, Principal principal) {
        String email = principal.getName();
        User user = userService.findUserByEmail(email);

        bankAccount.setUser(user);

        bankAccountRepository.save(bankAccount);

        return "redirect:/profile";
    }


}
