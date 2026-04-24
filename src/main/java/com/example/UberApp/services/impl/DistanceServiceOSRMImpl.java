package com.example.UberApp.services.impl;

import com.example.UberApp.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    private static final String OSRM_API_URL = "https://router.project-osrm.org/route/v1/driving/";

    @Override
    public double calculateDistance(Point src, Point dest) {
        //TODO call third party API called OSRM to calculate distance between src and dest
        try {

            String uri = src.getX() + "," + src.getY() + ";" +dest.getX() + "," + dest.getY();
            OSRMResponseDto osrmResponseDto = RestClient
                    .builder()
                    .baseUrl(OSRM_API_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDto.class);
            return osrmResponseDto.getRoutes().get(0).getDistance() / 1000.0; // Convert meters to kilometers
        }catch (Exception e){
            throw new RuntimeException("Error getting data from OSRM : " + e.getMessage());
        }
    }

}

@Data
class OSRMResponseDto {

    private List<OSRMRoutes> routes;
}

@Data
class OSRMRoutes {
    private double distance;
}