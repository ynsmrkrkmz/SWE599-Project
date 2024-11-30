package com.swe599.ramp.repository;

import com.swe599.ramp.model.ResearcherList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearcherListRepository extends JpaRepository<ResearcherList, Long> {

}
