package com.chatchatabc.parking.domain.repository.file;

import com.chatchatabc.parking.domain.model.file.CloudFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudFileRepository extends JpaRepository<CloudFile, Long> {
}
