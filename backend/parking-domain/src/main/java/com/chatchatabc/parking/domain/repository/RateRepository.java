package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
}
