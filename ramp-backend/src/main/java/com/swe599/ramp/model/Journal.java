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
@Table(name = "journal")
@RequiredArgsConstructor
@AllArgsConstructor
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "short_title", nullable = true)
    private String shortTitle;

    @Column(name = "full_title", nullable = true)
    private String fullTitle;

    @Column(name = "issn", nullable = true)
    private String issn;

    @Column(name = "eissn", nullable = true)
    private String eissn;

    @Column(name = "payment", nullable = true)
    private Double payment;

    @Column(name = "year", nullable = true)
    private Integer year;

    @Column(name = "mep", nullable = true)
    private Double mep;

    @Column(name = "sci", nullable = true)
    private String sci; // DOĞRU/YANLIŞ

    @Column(name = "soc", nullable = true)
    private String soc; // DOĞRU/YANLIŞ

    @Column(name = "ahci", nullable = true)
    private String ahci; // DOĞRU/YANLIŞ

    @Column(name = "source", nullable = true)
    private String source;
}
