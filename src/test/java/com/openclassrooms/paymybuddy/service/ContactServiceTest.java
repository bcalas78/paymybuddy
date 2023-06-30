package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private ContactService contactService;

    @Test
    public void testGetContact() {

        MockitoAnnotations.openMocks(this);

        User user = new User();
        User buddy = new User();

        when(contactRepository.findByUserAndBuddy(user, buddy)).thenReturn(Optional.empty());

        Optional<Contact> result = contactService.getContact(user, buddy);

        assertFalse(result.isPresent());

        verify(contactRepository).findByUserAndBuddy(user, buddy);
        verifyNoMoreInteractions(contactRepository, userRepository);
    }

    @Test
    public void testAddBuddy() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        User buddy = new User();

        assertDoesNotThrow(() -> {
            contactService.addBuddy(user, buddy);
        });

        verify(contactRepository).save(any(Contact.class));
        verifyNoMoreInteractions(contactRepository, userRepository);
    }

    @Test
    public void testGetContacts() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setUser_id(10);

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact());

        when(contactRepository.findByUser(user)).thenReturn(contacts);

        List<Contact> result = contactService.getContacts(user);

        assertEquals(contacts, result);

        verify(contactRepository).findByUser(user);
        verifyNoMoreInteractions(contactRepository, userRepository);
    }
}
