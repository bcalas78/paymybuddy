package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.BankAccountDto;
import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    public Iterable<BankAccount> getBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> getBankAccountById(Integer id) {
        return bankAccountRepository.findById(id);
    }

    public void createBankAccount(BankAccountDto bankAccountDto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBank_amount(bankAccountDto.getBank_amount());

        User user = userRepository.findById(bankAccountDto.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        bankAccount.setUser(user);
        bankAccountRepository.save(bankAccount);
    }
}
