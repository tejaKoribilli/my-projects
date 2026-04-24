package com.example.UberApp.dto;

import com.example.UberApp.entities.enums.PaymentMethod;
import com.example.UberApp.entities.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRideDto {

    private Long id;

    private PointDto pickupLocation;

    private PointDto dropoffLocation;

    private LocalDateTime createdTime;
    private RiderDto rider;
    private DriverDto driver;
    private PaymentMethod paymentMethod;

    private RideStatus rideStatus;

    private Double fare;
    private String otp;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
