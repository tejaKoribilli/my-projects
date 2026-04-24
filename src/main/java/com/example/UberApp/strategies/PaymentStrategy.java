package com.example.UberApp.strategies;

import com.example.UberApp.entities.Payment;

public interface PaymentStrategy {

    static final Double PLATFORM_COMISSION = 0.3;

    void processPayment(Payment payment);
}
