package com.sam.banking.service;

import com.sam.banking.model.Account;
import com.sam.banking.model.Transaction;
import com.sam.banking.repository.AccountRepository;
import com.sam.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    final private PasswordEncoder passwordEncoder;
    final private AccountRepository accountRepository;
    final private TransactionRepository transactionRepository;

    public Account findAccountByUsername(String username){
        return accountRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Account not Found!"));
    }

    public void registerAccount(String username, String password){
        if(accountRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username already Exist!");
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
    }

    public void deposit(Account account,BigDecimal amount){
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType("Deposit");
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

    }

    public void withdraw(Account account,BigDecimal amount){
        if(account.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Insufficient Funds!");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType("Withdrawal");
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

    }

    public void transferAmount(Account fromAccount,String toUsername,BigDecimal amount){
        if(fromAccount.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Insufficient Funds!");
        }

        Account toAccount = accountRepository.findByUsername(toUsername)
                .orElseThrow(()-> new RuntimeException("Recipient Account Not Found!"));
        //deduct
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        Transaction debitTrans = new Transaction();
        debitTrans.setAccount(fromAccount);
        debitTrans.setType("Transfer Out to " + toAccount.getUsername());
        debitTrans.setAmount(amount);
        debitTrans.setTimestamp(LocalDateTime.now());

        transactionRepository.save(debitTrans);

        Transaction creditTrans = new Transaction();
        creditTrans.setAccount(fromAccount);
        creditTrans.setType("Credited from " + fromAccount.getUsername());
        creditTrans.setAmount(amount);
        creditTrans.setTimestamp(LocalDateTime.now());

        transactionRepository.save(creditTrans);
    }


    public List<Transaction> getTransactionHistory(Account account){
        return transactionRepository.findByAccountId(account.getId());
    }
}
