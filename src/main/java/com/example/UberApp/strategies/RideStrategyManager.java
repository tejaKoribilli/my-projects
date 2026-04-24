package com.example.UberApp.strategies;

import com.example.UberApp.strategies.Impl.DriverMatchingHighestRatedDriverStrategy;
import com.example.UberApp.strategies.Impl.DriverMatchingNearestDriverStrategy;
import com.example.UberApp.strategies.Impl.RideFareDefaultFareCalculationStrategy;
import com.example.UberApp.strategies.Impl.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;

    public DriverMatchingStartegy driverMatchingStartegy(double rating){
        if(rating >= 4.8){
            return highestRatedDriverStrategy;
        }
        return nearestDriverStrategy;
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy(){
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();
        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if(isSurgeTime){
            return surgePricingFareCalculationStrategy;
        }
        return defaultFareCalculationStrategy;
    }

    
}
