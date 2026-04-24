package com.example.UberApp.strategies.Impl;

import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.RideRequest;
import com.example.UberApp.repositories.DriverRepository;
import com.example.UberApp.strategies.DriverMatchingStartegy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStartegy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDrivers(RideRequest rideRequest) {
        return driverRepository.findTenNearestDrivers(rideRequest.getPickupLocation());
    }
}
