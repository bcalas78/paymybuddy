package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.model.*;
import com.openclassrooms.paymybuddy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Optional;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class PayMyBuddyApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private FeeService feeService;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Iterable<User> users = userService.getUsers();
		users.forEach(user -> System.out.println(user.getFirst_name() + ' ' + user.getLast_name()));

		Optional<User> optUser = userService.getUserById(1);
		User user1 = optUser.get();
		System.out.println(user1.getFirst_name() + ' ' + user1.getLast_name());

		Iterable<Role> roles = roleService.getRoles();
		roles.forEach(role -> System.out.println(role.getName()));

		Optional<Role> optRole = roleService.getRoleById(1);
		Role role1 = optRole.get();
		System.out.println(role1.getName());

		Iterable<Transaction> transactions = transactionService.getTransactions();
		transactions.forEach(transaction -> System.out.println(transaction.getDescription()));

		Optional<Transaction> optTransaction = transactionService.getTransactionById(1);
		Transaction transaction1 = optTransaction.get();
		System.out.println(transaction1.getDescription());

		Iterable<BankAccount> bankAccounts = bankAccountService.getBankAccounts();
		bankAccounts.forEach(bankAccount -> System.out.println(bankAccount.getBank_amount()));

		Optional<BankAccount> optBankAccount = bankAccountService.getBankAccountById(2);
		BankAccount bankAccount2 = optBankAccount.get();
		System.out.println(bankAccount2.getBank_amount());

		Iterable<Contact> contacts = contactService.getContacts();
		contacts.forEach(contact -> System.out.println(contact.getContact_id()));

		Optional<Contact> optContact = contactService.getContactById(3);
		Contact contact3 = optContact.get();
		System.out.println(contact3.getContact_id());

		Iterable<Fee> fees = feeService.getFees();
		fees.forEach(fee -> System.out.println(fee.getFee_amount()));

		Optional<Fee> optFee = feeService.getFeeById(1);
		Fee fee1 = optFee.get();
		System.out.println(fee1.getFee_amount());
	}
}
