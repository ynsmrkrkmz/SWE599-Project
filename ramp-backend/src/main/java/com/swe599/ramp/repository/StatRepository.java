package com.swe599.ramp.repository;

import com.swe599.ramp.model.Stat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    Optional<Stat> findByResearcherId(Long researcherId);
}
