package com.example.UberApp.services;

import com.example.UberApp.dto.RideDto;
import com.example.UberApp.dto.RideRequestDto;
import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.Ride;
import com.example.UberApp.entities.RideRequest;
import com.example.UberApp.entities.Rider;
import com.example.UberApp.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long id);

    void matchWithDrivers(RideRequestDto rideRequestDto);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Ride updateRideStatus(Ride ride, RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Long driverId, PageRequest pageRequest);

    RideDto saveRide(Ride ride);

}
