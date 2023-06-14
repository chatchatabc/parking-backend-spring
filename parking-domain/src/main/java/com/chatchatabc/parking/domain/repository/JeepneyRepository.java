package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Jeepney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JeepneyRepository extends JpaRepository<Jeepney, Long> {
}
