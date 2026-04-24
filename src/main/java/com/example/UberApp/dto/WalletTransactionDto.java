package com.example.UberApp.dto;

import com.example.UberApp.entities.Ride;
import com.example.UberApp.entities.enums.TransactionMethod;
import com.example.UberApp.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransactionDto {

    private Long id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;

    private Ride ride;

    private String transactionId;

    private WalletDto wallet;

    private LocalDateTime timestamp;

}
