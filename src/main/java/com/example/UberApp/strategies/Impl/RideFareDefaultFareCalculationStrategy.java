package com.example.UberApp.strategies.Impl;

import com.example.UberApp.entities.RideRequest;
import com.example.UberApp.services.DistanceService;
import com.example.UberApp.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareDefaultFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(
                rideRequest.getPickupLocation(), rideRequest.getDropoffLocation());


        return distance*RIDE_FARE_MULTIPLIER;
    }

}
