package com.example.UberApp.services.impl;

import com.example.UberApp.dto.DriverDto;
import com.example.UberApp.dto.RiderDto;
import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.Rating;
import com.example.UberApp.entities.Ride;
import com.example.UberApp.entities.Rider;
import com.example.UberApp.exceptions.ResourceNotFoundException;
import com.example.UberApp.exceptions.RuntimeConflictException;
import com.example.UberApp.repositories.DriverRepository;
import com.example.UberApp.repositories.RatingRepository;
import com.example.UberApp.repositories.RiderRepository;
import com.example.UberApp.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {
        Rider rider = ride.getRider();
        Rating rideRating = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating for this ride not found with ride id : " + ride.getId()));

        if(rideRating.getRiderRating() != null){
            throw new RuntimeConflictException("Rider has already been rated for this ride");
        }

        rideRating.setRiderRating(rating);

        ratingRepository.save(rideRating);

        List<Rating> riderRatingList = ratingRepository.findByRider(rider);

        Double riderRating = riderRatingList.stream()
                .mapToDouble(Rating::getRiderRating)
                .average()
                .orElse(0.0);
        rider.setRating(riderRating);

        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }

    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        Driver driver = ride.getDriver();
        Rating rideRating = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating for this ride not found with ride id : " + ride.getId()));
        if(rideRating.getDriverRating() != null){
            throw new RuntimeConflictException("Driver has already been rated for this ride");
        }

        rideRating.setDriverRating(rating);

        ratingRepository.save(rideRating);

        List<Rating> driverRatingList = ratingRepository.findByDriver(driver);

        Double riderRating = driverRatingList.stream()
                .mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);
        driver.setRating(riderRating);

        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .ride(ride)
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .build();

        ratingRepository.save(rating);
    }

}
