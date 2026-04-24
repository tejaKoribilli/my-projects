package com.example.UberApp.services;

import com.example.UberApp.dto.DriverDto;
import com.example.UberApp.dto.RiderDto;
import com.example.UberApp.entities.Ride;

public interface RatingService {

    RiderDto rateRider(Ride ride, Integer rating);

    DriverDto rateDriver(Ride ride, Integer rating);

    void createNewRating(Ride ride);

}
