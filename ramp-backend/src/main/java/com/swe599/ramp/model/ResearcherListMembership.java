package com.swe599.ramp.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@Entity
@Table(name = "researcher_list_membership")
@RequiredArgsConstructor
@AllArgsConstructor
public class ResearcherListMembership extends BaseEntity {

    @EmbeddedId
    private ResearcherListMembershipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("researcher_id")
    @JoinColumn(name = "researcher_id", insertable = false, updatable = false)
    @Exclude
    private Researcher researcher;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("researcher_list_id")
    @JoinColumn(name = "researcher_list_id", insertable = false, updatable = false)
    @Exclude
    private ResearcherList researcherList;
}
