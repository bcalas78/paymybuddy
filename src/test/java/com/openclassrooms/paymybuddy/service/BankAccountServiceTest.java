package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.BankAccountDto;
import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @Test
    public void testCreateBankAccount() throws Exception {
        MockitoAnnotations.openMocks(this);

        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setBank_amount(100.0F);
        bankAccountDto.setUser_id(10);

        User user = new User();
        user.setUser_id(10);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        bankAccountService.createBankAccount(bankAccountDto);

        verify(userRepository).findById(10);
        verify(bankAccountRepository).save(any(BankAccount.class));

        verifyNoMoreInteractions(userRepository, bankAccountRepository);
    }

    @Test
    public void testCreateBankAccount_UserNotFound() {
        MockitoAnnotations.openMocks(this);

        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setBank_amount(100.0F);
        bankAccountDto.setUser_id(10);

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            bankAccountService.createBankAccount(bankAccountDto);
        });

        verify(userRepository).findById(10);

        verifyNoMoreInteractions(userRepository, bankAccountRepository);
    }
}

