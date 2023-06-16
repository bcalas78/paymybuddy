package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.ContactNotFoundException;
import com.openclassrooms.paymybuddy.exceptions.InsufficientFundsException;
import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Fee;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.repository.FeeRepository;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FeeRepository feeRepository;

    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    public void makeTransaction(User user, User buddy, float amount, String description) {
        try {
            float feeAmount = calculateFee(amount);
            float totalAmount = amount + feeAmount;

            if (user.getUser_amount() < totalAmount) {
                throw new InsufficientFundsException("Not enough balance to make the transaction.");
            }

            user.setUser_amount(user.getUser_amount() - totalAmount);
            userRepository.save(user);

            Contact contact = contactRepository.findByUserAndBuddy(user, buddy)
                    .orElseThrow(() -> new ContactNotFoundException("Contact not found between user and buddy."));

            Transaction transaction = new Transaction();
            transaction.setContact(contact);
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transactionRepository.save(transaction);

            Fee fee = new Fee();
            fee.setTransaction(transaction);
            fee.setFee_amount(feeAmount);
            feeRepository.save(fee);

            buddy.setUser_amount(buddy.getUser_amount() + amount);
            userRepository.save(buddy);

        } catch (InsufficientFundsException e) {
            e.printStackTrace();
            throw e;
        } catch (ContactNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private float calculateFee(float amount) {
        float feePercentage = 0.5f;
        return amount * (feePercentage / 100);
    }
}
