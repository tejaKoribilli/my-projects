package com.example.UberApp.dto;

import java.util.List;

public class WalletDto {

    private Long id;

    private UserDto user;

    private Double balance = 0.0;

    private List<WalletTransactionDto> transaction;
}
