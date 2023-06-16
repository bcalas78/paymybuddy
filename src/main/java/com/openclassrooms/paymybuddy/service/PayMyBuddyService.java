package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayMyBuddyService {
    @Autowired
    private ContactService contactService;

    public void addBuddyToUser(User user, User buddy) {
        contactService.addBuddy(user, buddy);
    }
}
