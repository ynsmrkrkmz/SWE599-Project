package com.swe599.ramp.repository;

import com.swe599.ramp.model.Journal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {

    boolean existsByIssn(String issn);
    boolean existsByEissn(String eissn);
}
