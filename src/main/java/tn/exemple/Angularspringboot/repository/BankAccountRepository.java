package tn.exemple.Angularspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.exemple.Angularspringboot.entity.Customer;
import tn.exemple.Angularspringboot.entity.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
