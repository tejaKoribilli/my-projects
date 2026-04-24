package com.example.UberApp.services;

import com.example.UberApp.entities.Payment;
import com.example.UberApp.entities.Ride;
import com.example.UberApp.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);

}
