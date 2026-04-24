package com.example.UberApp.strategies;


import com.example.UberApp.entities.enums.PaymentMethod;
import com.example.UberApp.strategies.Impl.CashPaymentStrategy;
import com.example.UberApp.strategies.Impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final CashPaymentStrategy cashPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;

    public PaymentStrategy getPaymentStrategy(PaymentMethod paymentMethod){
        return switch (paymentMethod) {
            case CASH -> cashPaymentStrategy;
            case WALLET -> walletPaymentStrategy;
        };
    }
}
