package com.example.UberApp.services;

import com.example.UberApp.dto.DriverDto;
import com.example.UberApp.dto.DriverRideDto;
import com.example.UberApp.dto.RideDto;
import com.example.UberApp.dto.RiderDto;
import com.example.UberApp.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DriverService {

    DriverRideDto acceptRide(Long rideRequestId);

    DriverRideDto cancelRide(Long rideId);

    DriverRideDto startRide(Long rideId, String otp);

    DriverRideDto endRide(Long rideId);

    RiderDto rateRider(Long rideId, Integer rating);

    DriverDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Driver updateDriverAvailability(Driver driver, boolean avaliable);

    Driver createNewDriver(Driver driver);
}
