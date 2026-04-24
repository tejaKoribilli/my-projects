package com.example.UberApp.services.impl;

import com.example.UberApp.entities.RideRequest;
import com.example.UberApp.exceptions.ResourceNotFoundException;
import com.example.UberApp.repositories.RideRequestRepository;
import com.example.UberApp.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with id : " + rideRequestId));
    }

    @Override
    public void update(RideRequest rideRequest) {
        rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with id : " + rideRequest.getId()));
        rideRequestRepository.save(rideRequest);
    }


}
