package com.example.UberApp.services;

import com.example.UberApp.dto.DriverDto;
import com.example.UberApp.dto.SignupDto;
import com.example.UberApp.dto.UserDto;

public interface AuthService {

    String[] login(String email, String password);

    UserDto signup(SignupDto signupDto);

    DriverDto onboardNewDriver(Long userId, String vehicleId);

    String refreshToken(String refreshToken);
}
