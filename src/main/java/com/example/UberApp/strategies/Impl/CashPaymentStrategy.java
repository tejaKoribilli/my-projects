package com.example.UberApp.strategies.Impl;

import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.Payment;
import com.example.UberApp.entities.enums.PaymentStatus;
import com.example.UberApp.entities.enums.TransactionMethod;
import com.example.UberApp.repositories.PaymentRepository;
import com.example.UberApp.services.WalletService;
import com.example.UberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        double platformComission  = payment.getAmount() * PLATFORM_COMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(), platformComission, null ,
                payment.getRide(), TransactionMethod.Ride);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
