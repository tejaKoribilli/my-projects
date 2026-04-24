package com.example.UberApp.strategies.Impl;

import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.Payment;
import com.example.UberApp.entities.Rider;
import com.example.UberApp.entities.enums.PaymentStatus;
import com.example.UberApp.entities.enums.TransactionMethod;
import com.example.UberApp.repositories.PaymentRepository;
import com.example.UberApp.services.WalletService;
import com.example.UberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider =  payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null,
                payment.getRide(), TransactionMethod.Ride);

        double driversCut = payment.getAmount() * (1 - PLATFORM_COMISSION);
        walletService.addMoneyToWallet(driver.getUser(), driversCut, null,
                payment.getRide(), TransactionMethod.Ride);


        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

    }
}
