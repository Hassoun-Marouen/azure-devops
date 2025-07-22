package tn.exemple.Angularspringboot.service;

import tn.exemple.Angularspringboot.Dto.AccountHistoryDTO;
import tn.exemple.Angularspringboot.Dto.AccountOperationDTO;
import tn.exemple.Angularspringboot.Dto.BankAccountDTO;
import tn.exemple.Angularspringboot.Dto.CurrentBankAccountDTO;
import tn.exemple.Angularspringboot.Dto.CustomerDTO;
import tn.exemple.Angularspringboot.Dto.SavingBankAccountDTO;
import tn.exemple.Angularspringboot.Exceptions.BalanceNotSufficientException;
import tn.exemple.Angularspringboot.Exceptions.BankAccountNotFoundException;
import tn.exemple.Angularspringboot.Exceptions.CustomerNotFoundException;
import tn.exemple.Angularspringboot.entity.BankAccount;

import java.util.List;

public interface BankAccountService {
	CustomerDTO saveCustomer(CustomerDTO customerDTO);

	CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
			throws CustomerNotFoundException;

	SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
			throws CustomerNotFoundException;

	List<CustomerDTO> listCustomers();

	BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

	void debit(String accountId, double amount, String description)
			throws BankAccountNotFoundException, BalanceNotSufficientException;

	void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

	void transfer(String accountIdSource, String accountIdDestination, double amount)
			throws BankAccountNotFoundException, BalanceNotSufficientException;

	List<BankAccountDTO> bankAccountList();

	CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

	CustomerDTO updateCustomer(CustomerDTO customerDTO);

	void deleteCustomer(Long customerId);

	List<AccountOperationDTO> accountHistory(String accountId);

	AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

	List<CustomerDTO> searchCustomers(String keyword);
	List<BankAccountDTO> getAccountsByCustomerID(Long customerID)throws CustomerNotFoundException;
}
