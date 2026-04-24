package com.example.UberApp.repositories;

import com.example.UberApp.entities.Driver;
import com.example.UberApp.entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Geospatial queries using PostGIS functions
//ST_Distance(point1, point2)
//ST_DWithin(point1, 10000)

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS distance FROM driver d " +
    "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
    "ORDER by distance LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearestDrivers(Point pickupLocation);

    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS distance FROM driver AS d " +
            "WHERE available = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
            "ORDER by rating DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenHighRatedDrivers(Point pickupLocation);

    Optional<Driver> findByUser(User user);

}
