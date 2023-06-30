package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.UserNotFoundException;
import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    public Optional<Contact> getContact(User user, User buddy) {
        return contactRepository.findByUserAndBuddy(user, buddy);
    }

    /*public void addContact(User buddy) {
        User currentUser = userServiceImpl.getCurrentUser();

        if (contactRepository.existsByUserAndBuddy(currentUser, buddy)) {
            throw new ContactAlreadyExistsException("Contact already exists");
        }

        Contact contact = new Contact();
        contact.setUser(currentUser);
        contact.setBuddy(buddy);
        contactRepository.save(contact);
    }*/

    public void addBuddy(User user, User buddy) throws Exception {
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setBuddy(buddy);

        contactRepository.save(contact);
    }

    public List<Contact> getContacts(User user) {
        return contactRepository.findByUser(user);
    }

    public void sendContactRequest(User user, User buddy) throws Exception {
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setBuddy(buddy);

        user.getSentContacts().add(contact);

        buddy.getBuddies().add(contact);

        if (user == null || buddy == null) {
            throw new UserNotFoundException("User or buddy not found!");
        }

        boolean requestExists = contactRepository.existsByUserAndBuddy(user, buddy);

        if (requestExists) {
            throw new DuplicateRequestException("Contact request already sent!");
        }

        userRepository.save(user);
        userRepository.save(buddy);
    }
}
