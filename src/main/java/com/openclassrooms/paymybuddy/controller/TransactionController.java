package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.exceptions.ContactNotFoundException;
import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.TransactionForm;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.ContactService;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/transactions")
    public String showTransactions(Model model, Principal principal, @RequestParam(defaultValue = "1") int page) {
        try {
            int size = 5;
            List<Transaction> pagingTransactions = new ArrayList<Transaction>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Transaction> transactionPage = transactionRepository.findAll(paging);

            pagingTransactions = transactionPage.getContent();

            model.addAttribute("pagingTransactions", pagingTransactions);
            model.addAttribute("currentPage", transactionPage.getNumber() + 1);
            model.addAttribute("totalItems", transactionPage.getTotalElements());
            model.addAttribute("totalPages", transactionPage.getTotalPages());
            model.addAttribute("pageSize", size);

            String email = principal.getName();
            User currentUser = userService.findUserByEmail(email);

            List<Contact> buddies = currentUser.getBuddies();
            List<Contact> contacts = contactService.getContacts(currentUser);

            Iterable<Transaction> transactionIterable = transactionService.getTransactionsByUser(currentUser);
            List<Transaction> transactions = StreamSupport.stream(transactionIterable.spliterator(), false)
                    .collect(Collectors.toList());

            model.addAttribute("buddies", buddies);
            model.addAttribute("contacts", contacts);
            model.addAttribute("transactionForm", new TransactionForm());
            model.addAttribute("transactions", transactions);
            model.addAttribute("currentUser", currentUser);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "transaction-page";
    }

    @PostMapping("/transactions")
    public String createTransaction(@RequestParam("buddyEmail") String buddyEmail,
                                    @ModelAttribute("transactionForm") TransactionForm form,
                                    Model model,
                                    Principal principal) {
        try {
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
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "redirect:/transactions";
    }
}
