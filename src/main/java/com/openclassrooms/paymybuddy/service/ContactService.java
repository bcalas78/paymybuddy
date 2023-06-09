package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.ContactAlreadyExistsException;
import com.openclassrooms.paymybuddy.exceptions.ContactNotFoundException;
import com.openclassrooms.paymybuddy.exceptions.UserNotFoundException;
import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public Iterable<Contact> getContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(Integer id) {
        return contactRepository.findById(id);
    }

    public void addContact(User user, User buddy) {

        if (contactRepository.existsByUserAndBuddy(user, buddy)) {
            throw new ContactAlreadyExistsException("Contact already exists");
        }

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setBuddy(buddy);
        contactRepository.save(contact);
    }

    public List<User> getContacts(User user) {
        List<Contact> contacts = contactRepository.findByUser(user);
        return  contacts.stream()
                .map(Contact::getBuddy)
                .collect(Collectors.toList());
    }

    public void acceptContactRequest(Integer contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));

        User user = contact.getUser();
        User buddy = contact.getBuddy();

        addContact(user, buddy);

        contactRepository.delete(contact);
    }

    public void declineContactRequest(Integer contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));

        contactRepository.delete(contact);
    }

    public void sendContactRequest(String buddyEmail) {
        User user = userService.getCurrentUser();
        User buddy = userRepository.findByEmail(buddyEmail);

        if (user == null || buddy == null) {
            throw new UserNotFoundException("User or buddy not found!");
        }

        boolean requestExists = contactRepository.existsByUserAndBuddy(user, buddy);

        if (requestExists) {
            throw new DuplicateRequestException("Contact request already sent!");
        }

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setBuddy(buddy);

        contactRepository.save(contact);
    }
}
