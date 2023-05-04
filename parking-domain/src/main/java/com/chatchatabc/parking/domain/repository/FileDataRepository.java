package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepository extends JpaRepository<File, String> {
    /**
     * Find a file by parent id and bit value
     *
     * @param parentId the parent id of the file
     * @param divisor  the divisor of the flag
     * @param bitValue the bit value of the flag
     * @param pageable the pageable
     * @return the page of files
     */
    @Query("SELECT f FROM File f WHERE f.parentId = :parentId AND MOD(f.flag / :divisor, 2) = :bitValue")
    Page<File> findAllByParentIdAndFlag(String parentId, int divisor, int bitValue, Pageable pageable);
}
