package com.example.UberApp.services.impl;

import com.example.UberApp.dto.DriverDto;
import com.example.UberApp.dto.SignupDto;
import com.example.UberApp.dto.UserDto;
import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.User;
import com.example.UberApp.entities.enums.Role;
import com.example.UberApp.exceptions.ResourceNotFoundException;
import com.example.UberApp.exceptions.RuntimeConflictException;
import com.example.UberApp.repositories.UserRepository;
import com.example.UberApp.security.JWTService;
import com.example.UberApp.services.AuthService;
import com.example.UberApp.services.DriverService;
import com.example.UberApp.services.RiderService;
import com.example.UberApp.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.example.UberApp.entities.enums.Role.DRIVER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public String[] login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken, refreshToken};
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        User userExists = userRepository.findByEmail(signupDto.getEmail()).orElse(null);
        if(userExists != null){
            throw new RuntimeConflictException("User with email "+signupDto.getEmail()+" already exists");
        }

        User user = modelMapper.map(signupDto, User.class);
        user.setRoles(Set.of(Role.RIDER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        //create new user related entities here
        riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    @Transactional
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id "+userId+" not found"));

        if(user.getRoles().contains(DRIVER))
            throw new RuntimeConflictException("User with id "+userId+" is already a Driver");

        user.getRoles().add(DRIVER);
        userRepository.save(user);

        Driver driver = Driver.builder()
                .user(user)
                .rating(0.0)
                .available(true)
                .vehicleId(vehicleId)
                .build();

        Driver savedDriver = driverService.createNewDriver(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id "+userId+" not found"));
        return jwtService.generateAccessToken(user);
    }
}
