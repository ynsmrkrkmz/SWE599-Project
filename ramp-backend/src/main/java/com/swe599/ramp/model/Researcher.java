package com.swe599.ramp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "researcher")
@RequiredArgsConstructor
@AllArgsConstructor
public class Researcher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "open_alex_id", nullable = false)
    private String openAlexId;

    @Column(name = "orc_id", nullable = true)
    private String orcId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "institution", nullable = true)
    private String institution;

    @Column(name = "institution_country", nullable = true)
    private String institutionCountry;
}
