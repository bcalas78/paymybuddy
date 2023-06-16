package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.ContactService;
import com.openclassrooms.paymybuddy.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/contact-requests")
    public String contactRequests(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findUserByEmail(email);

        List<Contact> contacts = contactService.getContacts(user);
        model.addAttribute("contacts", contacts);

        return "contact-requests-page";
    }

    @GetMapping("/add-contact")
    public String showAddContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "add-contact";
    }

    @PostMapping("/add-contact")
    public String sendContactRequest(@RequestParam("buddyEmail") String buddyEmail, Principal principal) {
        String userEmail = principal.getName();

        User user = userService.findUserByEmail(userEmail);
        User buddy = userService.findUserByEmail(buddyEmail);

        if(user != null && buddy != null) {
            contactService.sendContactRequest(user, buddy);
        }

        return "redirect:/contact-requests";
    }

     /*@PostMapping("/contact-requests/accept")
    public String acceptContactRequest(@RequestParam("contact_id") Integer contact_id) {
        contactService.acceptContactRequest(contact_id);
        return  "redirect:/contact-requests";
    }*/

    /*@PostMapping("/contact-requests/decline")
    public String declineContactRequest(@RequestParam("contact_id") Integer contact_id) {
        contactService.declineContactRequest(contact_id);
        return  "redirect:/contact-requests";
    }*/
}
