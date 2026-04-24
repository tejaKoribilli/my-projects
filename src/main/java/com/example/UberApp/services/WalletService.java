package com.example.UberApp.services;

import com.example.UberApp.entities.Ride;
import com.example.UberApp.entities.User;
import com.example.UberApp.entities.Wallet;
import com.example.UberApp.entities.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId,
                            Ride ride, TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId,
                                 Ride ride, TransactionMethod transactionMethod);

    void WithdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);
}
