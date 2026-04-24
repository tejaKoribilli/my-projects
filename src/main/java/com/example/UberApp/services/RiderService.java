package com.example.UberApp.services;

import com.example.UberApp.dto.DriverDto;
import com.example.UberApp.dto.RideDto;
import com.example.UberApp.dto.RideRequestDto;
import com.example.UberApp.dto.RiderDto;
import com.example.UberApp.entities.Rider;
import com.example.UberApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId, Integer rating);

    RiderDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User savedUser);

    Rider getCurrentRider();

    RiderDto saveRider(Rider rider);
}
