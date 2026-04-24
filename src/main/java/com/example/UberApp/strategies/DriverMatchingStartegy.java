package com.example.UberApp.strategies;

import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStartegy {

    List<Driver> findMatchingDrivers(RideRequest rideRequest);
}
