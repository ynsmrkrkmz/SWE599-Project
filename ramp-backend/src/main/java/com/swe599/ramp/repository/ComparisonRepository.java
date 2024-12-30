package com.swe599.ramp.repository;

import com.swe599.ramp.model.Comparison;
import com.swe599.ramp.model.ResearcherList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComparisonRepository extends JpaRepository<Comparison, Long> {

    Optional<Comparison> findByNameAndCreatedById(String name, Long createdById);

    List<Comparison> findAllByCreatedById(Long createdById);
}
