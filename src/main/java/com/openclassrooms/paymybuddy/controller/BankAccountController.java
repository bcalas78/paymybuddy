package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.exceptions.InsufficientFundsException;
import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.Fee;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.FeeRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.service.FeeService;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BankAccountController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal, @RequestParam(defaultValue = "1") int page) {
        try {
            int size = 5;
            List<Fee> pagingFees = new ArrayList<Fee>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Fee> feePage = feeRepository.findAll(paging);

            pagingFees = feePage.getContent();

            model.addAttribute("pagingFees", pagingFees);
            model.addAttribute("currentPage", feePage.getNumber() + 1);
            model.addAttribute("totalItems", feePage.getTotalElements());
            model.addAttribute("totalPages", feePage.getTotalPages());
            model.addAttribute("pageSize", size);

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
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "profile-page";
    }

    @PostMapping("/profile/saveBankAmount")
    public String saveBankAccount(@ModelAttribute("bank_account") BankAccount bankAccount, Model model, Principal principal) {
        try {
            String email = principal.getName();

            User user = userService.findUserByEmail(email);

            bankAccount.setUser(user);

            bankAccountRepository.save(bankAccount);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/addMoneyToBankAccount")
    public String addMoneyToBankAccount(@RequestParam("new_bank_amount") float bank_amount, Model model, Principal principal) {
        try {
            String email = principal.getName();

            User user = userService.findUserByEmail(email);

            BankAccount bankAccount = user.getBankAccount();

            bankAccount.setBank_amount(bankAccount.getBank_amount() + bank_amount);

            bankAccountRepository.save(bankAccount);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/withdrawMoney")
    public String withdrawMoney(@RequestParam("amount_from_bank_account") float amount, Model model, Principal principal) {
        try {
            String email = principal.getName();

            User user = userService.findUserByEmail(email);

            BankAccount bankAccount = user.getBankAccount();

            if(amount <= user.getUser_amount()) {
                user.setUser_amount(user.getUser_amount() - amount);
                bankAccount.setBank_amount(bankAccount.getBank_amount() + amount);
                bankAccountRepository.save(bankAccount);
                userRepository.save(user);
            } else {
                throw new InsufficientFundsException("You don't have enough money on your user account!");
            } } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/addMoneyToUserAccount")
    public String addMoneyToUserAccount(@RequestParam("amount_to_bank_account") float amount, Principal principal, Model model) {
        try {
            String email = principal.getName();

            User user = userService.findUserByEmail(email);

            BankAccount bankAccount = user.getBankAccount();

            if (amount <= bankAccount.getBank_amount()) {
                user.setUser_amount(user.getUser_amount() + amount);
                bankAccount.setBank_amount(bankAccount.getBank_amount() - amount);
                bankAccountRepository.save(bankAccount);
                userRepository.save(user);
            } else {
                throw new InsufficientFundsException("You don't have enough money on your bank account!");
            }
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/profile";
    }
}
