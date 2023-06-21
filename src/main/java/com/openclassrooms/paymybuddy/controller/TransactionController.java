package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.exceptions.ContactNotFoundException;
import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.TransactionForm;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.ContactService;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    ContactService contactService;

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/transactions")
    public String showTransactions(Model model) {
        User currentUser = userService.getCurrentUser();

        List<Contact> buddies = currentUser.getBuddies();

        Iterable<Transaction> transactionIterable = transactionService.getTransactions();
        List<Transaction> transactions = StreamSupport.stream(transactionIterable.spliterator(), false)
                        .collect(Collectors.toList());

        model.addAttribute("buddies", buddies);
        model.addAttribute("transactionForm", new TransactionForm());
        model.addAttribute("transactions", transactions);
        model.addAttribute("currentUser", currentUser);

        return "transaction-page";
    }

    @PostMapping("/transactions")
    public String createTransaction(@RequestParam("buddyEmail") String buddyEmail,
                                    @ModelAttribute("transactionForm") TransactionForm form,
                                    Principal principal) {
        String currentUserEmail = principal.getName();
        User currentUser = userService.findUserByEmail(currentUserEmail);
        User buddy = userService.findUserByEmail(buddyEmail);

        Optional<Contact> OptContact = contactService.getContact(currentUser, buddy);
        if (OptContact.isPresent()) {
            Contact contact = OptContact.get();
            Transaction transaction = transactionService.makeTransaction(contact, form.getAmount(), form.getDescription());
        } else {
            throw new ContactNotFoundException("Contact not found!");
        }

        return "redirect:/transactions";
    }
}
