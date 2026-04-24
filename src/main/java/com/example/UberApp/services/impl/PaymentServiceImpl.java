package com.example.UberApp.services.impl;

import com.example.UberApp.entities.Payment;
import com.example.UberApp.entities.Ride;
import com.example.UberApp.entities.enums.PaymentStatus;
import com.example.UberApp.exceptions.ResourceNotFoundException;
import com.example.UberApp.repositories.PaymentRepository;
import com.example.UberApp.services.PaymentService;
import com.example.UberApp.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final PaymentStrategyManager paymentStrategyManager;


    @Override
    public void processPayment(Ride ride) {

        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ride id: " + ride.getId()));
        paymentStrategyManager
                .getPaymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    @Transactional
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                            .ride(ride)
                            .amount(ride.getFare())
                            .paymentMethod(ride.getPaymentMethod())
                            .paymentStatus(PaymentStatus.PENDING)
                            .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
