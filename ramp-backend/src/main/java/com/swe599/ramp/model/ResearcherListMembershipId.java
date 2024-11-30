package com.swe599.ramp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ResearcherListMembershipId implements Serializable {

    @Column(name = "researcher_id")
    private Long researcherId;

    @Column(name = "researcher_list_id")
    private Long researcherListId;
}