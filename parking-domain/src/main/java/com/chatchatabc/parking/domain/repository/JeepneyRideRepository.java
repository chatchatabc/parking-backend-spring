package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.JeepneyRide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JeepneyRideRepository extends JpaRepository<JeepneyRide, Long> {
}
