package com.example.UberApp.services.impl;

import com.example.UberApp.dto.DriverDto;
import com.example.UberApp.dto.DriverRideDto;
import com.example.UberApp.dto.RideDto;
import com.example.UberApp.dto.RiderDto;
import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.Ride;
import com.example.UberApp.entities.RideRequest;
import com.example.UberApp.entities.User;
import com.example.UberApp.entities.enums.RideRequestStatus;
import com.example.UberApp.entities.enums.RideStatus;
import com.example.UberApp.exceptions.ResourceNotFoundException;
import com.example.UberApp.repositories.DriverRepository;
import com.example.UberApp.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;

    @Override
    @Transactional
    public DriverRideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
            throw new RuntimeException("Ride request cannot be accepted, status is : " + rideRequest.getRideRequestStatus());
        }

        Driver driver = getCurrentDriver();
        if(!driver.getAvailable()){
            throw new RuntimeException("Driver is not available to accept this ride request");
        }

        Driver savedDriver = updateDriverAvailability(driver, false);
        Ride ride = rideService.createNewRide(rideRequest, savedDriver);

        return modelMapper.map(ride, DriverRideDto.class);
    }

    @Override
    public DriverRideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot cancel this ride as he is not assigned to this ride");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be cancelled as the status is not CONFIRMED, status : " + ride.getRideStatus());
        }
        rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        updateDriverAvailability(driver, false);
        return modelMapper.map(ride, DriverRideDto.class);
    }

    @Override
    public DriverRideDto startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is not assigned to this ride");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be started as the status is not CONFIRMED, status : " + ride.getRideStatus());
        }
        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("Invalid OTP provided for starting the ride otp : " + otp);
        }
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(savedRide);

        return modelMapper.map(ride, DriverRideDto.class);
    }

    @Override
    @Transactional
    public DriverRideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot cancel this ride as he is not assigned to this ride");
        }

        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Ride cannot be ended as the status is not ongoing, status : " + ride.getRideStatus());
        }
        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);
        updateDriverAvailability(driver, true);

//        paymentService.createNewPayment(ride);
        paymentService.processPayment(savedRide);

        return modelMapper.map(savedRide, DriverRideDto.class);
    }

    @Override
    @Transactional
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = ride.getDriver();

        if(!driver.equals(getCurrentDriver())){
            throw new RuntimeException("Driver cannot rate the rider as he is not assigned to this ride");
        }
        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Driver cannot rate the rider as the ride is not ended yet, ride status : " + ride.getRideStatus());
        }

        return ratingService.rateRider(ride, rating);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver.getId(), pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }


    public Driver getCurrentDriver(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return driverRepository.findByUser(user).orElseThrow(() ->
                new ResourceNotFoundException(
                "Driver not associated with user with id: "+user.getId()
        ));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean avaliable) {
        driver.setAvailable(avaliable);
        return driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }

}
