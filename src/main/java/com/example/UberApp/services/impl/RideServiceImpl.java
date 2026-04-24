package com.example.UberApp.services.impl;

import com.example.UberApp.dto.RideDto;
import com.example.UberApp.dto.RideRequestDto;
import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.Ride;
import com.example.UberApp.entities.RideRequest;
import com.example.UberApp.entities.Rider;
import com.example.UberApp.entities.enums.RideRequestStatus;
import com.example.UberApp.entities.enums.RideStatus;
import com.example.UberApp.exceptions.ResourceNotFoundException;
import com.example.UberApp.repositories.RideRepository;
import com.example.UberApp.services.RideRequestService;
import com.example.UberApp.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final ModelMapper modelMapper;
    private final RideRepository rideRepository;
    private final RideRequestService rideRequestService;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride Not Found with rideId : " + rideId));
    }

    @Override
    public void matchWithDrivers(RideRequestDto rideRequestDto) {

    }

    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
        ride.setId(null);
        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider, pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Long driverId, PageRequest pageRequest) {
        return rideRepository.findByDriver(driverId, pageRequest);
    }

    @Override
    public RideDto saveRide(Ride ride) {
        Ride savedRide = rideRepository.save(ride);
        return modelMapper.map(savedRide, RideDto.class);
    }

    public String generateRandomOTP(){
        Random random = new Random();
        int otp = random.nextInt(10000);
        return String.format("%04d", otp);
    }

}
