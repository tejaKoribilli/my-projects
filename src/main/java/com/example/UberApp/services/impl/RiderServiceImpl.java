package com.example.UberApp.services.impl;

import com.example.UberApp.dto.DriverDto;
import com.example.UberApp.dto.RideDto;
import com.example.UberApp.dto.RideRequestDto;
import com.example.UberApp.dto.RiderDto;
import com.example.UberApp.entities.*;
import com.example.UberApp.entities.enums.RideRequestStatus;
import com.example.UberApp.entities.enums.RideStatus;
import com.example.UberApp.exceptions.ResourceNotFoundException;
import com.example.UberApp.repositories.RideRequestRepository;
import com.example.UberApp.repositories.RiderRepository;
import com.example.UberApp.services.DriverService;
import com.example.UberApp.services.RatingService;
import com.example.UberApp.services.RideService;
import com.example.UberApp.services.RiderService;
import com.example.UberApp.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider = getCurrentRider();

        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> driversList = rideStrategyManager
                .driverMatchingStartegy(rider.getRating()).findMatchingDrivers(rideRequest);
        //TODO : Send notifications to all drivers about this ride request

        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);
        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider does not own the ride with id : " + rideId);
        }
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Only confirmed rides can be cancelled");
        }
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);

        driverService.updateDriverAvailability(ride.getDriver(), true);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = ride.getRider();

        if(!rider.equals(getCurrentRider())){
            throw new RuntimeException("Rider cannot rate the rider as he is not assigned to this ride");
        }
        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Rider cannot rate the rider as the ride is not ended yet, ride status : " + ride.getRideStatus());
        }

        return ratingService.rateDriver(ride, rating);
    }

    @Override
    public RiderDto getMyProfile() {
        Rider currentRider = getCurrentRider();

        return modelMapper.map(currentRider, RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider, pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Rider createNewRider(User savedUser) {
        Rider rider = Rider
                .builder()
                .user(savedUser)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        // TODO : Implement Spring security
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return riderRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Rider not assosiated with user with id : " + user.getId()));
    }

    @Override
    public RiderDto saveRider(Rider rider) {
        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }
}
