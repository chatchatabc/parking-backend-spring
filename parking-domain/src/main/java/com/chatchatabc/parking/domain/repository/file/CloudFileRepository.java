package com.chatchatabc.parking.domain.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudFileRepository extends JpaRepository<CloudFileRepository, Long> {
}
