package com.swe599.ramp.repository;

import com.swe599.ramp.model.ResearcherListMembership;
import com.swe599.ramp.model.ResearcherListMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearcherListMembershipRepository extends
    JpaRepository<ResearcherListMembership, ResearcherListMembershipId> {

}
