package tn.exemple.Angularspringboot.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import tn.exemple.Angularspringboot.Dto.*;
import tn.exemple.Angularspringboot.Exceptions.*;
import tn.exemple.Angularspringboot.entity.BankAccount;
import tn.exemple.Angularspringboot.service.BankAccountService;

import java.util.List;

@RestController
public class BankAccountRestAPI {
	private BankAccountService bankAccountService;

	public BankAccountRestAPI(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

	@GetMapping("/accounts/{accountId}")
	public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
		return bankAccountService.getBankAccount(accountId);
	}

	@GetMapping("/accounts")
	public List<BankAccountDTO> listAccounts() {
		return bankAccountService.bankAccountList();
	}

	@GetMapping("/accounts/{accountId}/operations")
	public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
		return bankAccountService.accountHistory(accountId);
	}

	@GetMapping("/accounts/{accountId}/pageOperations")
	public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
		return bankAccountService.getAccountHistory(accountId, page, size);
	}

	@PostMapping("/accounts/debit")
	public DebitDTO debit(@RequestBody DebitDTO debitDTO)
			throws BankAccountNotFoundException, BalanceNotSufficientException {
		this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
		return debitDTO;
	}

	@PostMapping("/accounts/credit")
	public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
		this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
		return creditDTO;
	}

	@PostMapping("/accounts/transfer")
	public void transfer(@RequestBody TransferRequestDTO transferRequestDTO)
			throws BankAccountNotFoundException, BalanceNotSufficientException {
		this.bankAccountService.transfer(transferRequestDTO.getAccountSource(),
				transferRequestDTO.getAccountDestination(), transferRequestDTO.getAmount());
	}
	@GetMapping("/accounts/customerAccounts/{customerID}")
	public List<BankAccountDTO> getAccountsByCustomerID(@PathVariable Long customerID)throws CustomerNotFoundException{
		return bankAccountService.getAccountsByCustomerID(customerID);
	}
	@PostMapping("/accounts/saveCurrentAccount")
	public CurrentBankAccountDTO saveCurrentBankAccount(@RequestBody CurrentBankAccountDTO data ) throws CustomerNotFoundException {
		return bankAccountService.saveCurrentBankAccount(data.getBalance(),(double) data.getOverDraft(), data.getCustomerDTO().getId());
	}
	@PostMapping("/accounts/saveSavingAccount")
	public SavingBankAccountDTO saveSavingBankAccount(@RequestBody SavingBankAccountDTO data ) throws CustomerNotFoundException {
		return bankAccountService.saveSavingBankAccount(data.getBalance(),(double) data.getInterestRate(), data.getCustomerDTO().getId());
	}
}
