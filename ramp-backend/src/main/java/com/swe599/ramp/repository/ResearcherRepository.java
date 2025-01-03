package com.swe599.ramp.repository;

import com.swe599.ramp.model.Researcher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearcherRepository extends JpaRepository<Researcher, Long> {

    Optional<Researcher> findByOpenAlexId(String openAlexId);
}
