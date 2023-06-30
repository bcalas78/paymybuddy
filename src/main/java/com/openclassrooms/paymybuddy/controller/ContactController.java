package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.service.ContactService;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/contact")
    public String showAddContactForm(Model model, Principal principal, @RequestParam(defaultValue = "1") int page) {
        try {
            int size = 5;
            List<Contact> pagingContacts = new ArrayList<Contact>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Contact> contactPage = contactRepository.findAll(paging);

            pagingContacts = contactPage.getContent();

            model.addAttribute("pagingContacts", pagingContacts);
            model.addAttribute("currentPage", contactPage.getNumber() + 1);
            model.addAttribute("totalItems", contactPage.getTotalElements());
            model.addAttribute("totalPages", contactPage.getTotalPages());
            model.addAttribute("pageSize", size);

            String email = principal.getName();
            User user = userService.findUserByEmail(email);

            List<Contact> contacts = contactService.getContacts(user);
            model.addAttribute("contacts", contacts);
            model.addAttribute("contact", new Contact());
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "contact-page";
    }

    @PostMapping("/contact")
    public String sendContactRequest(@RequestParam("buddyEmail") String buddyEmail, Model model, Principal principal) {
        try {
            String userEmail = principal.getName();

            User user = userService.findUserByEmail(userEmail);
            User buddy = userService.findUserByEmail(buddyEmail);

            if(user != null && buddy != null) {
                contactService.sendContactRequest(user, buddy);
            }
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "redirect:/contact-page";
    }

}
