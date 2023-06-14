package com.chatchatabc.parking.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JeepneyRepository extends JpaRepository<JeepneyRepository, Long> {
}
