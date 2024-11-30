package com.swe599.ramp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "stat")
@RequiredArgsConstructor
@AllArgsConstructor
public class Stat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "researcher_id", nullable = false)
    @Exclude
    private Researcher researcher;

    @Column(name = "result_count", nullable = false)
    private Integer resultCount;

    @Column(name = "year", nullable = false, columnDefinition = "text")
    private String year;

    @Column(name = "open_access", nullable = false, columnDefinition = "text")
    private String openAccess;

    @Column(name = "topic", nullable = false, columnDefinition = "text")
    private String topic;

    @Column(name = "institution", nullable = false, columnDefinition = "text")
    private String institution;

    @Column(name = "type", nullable = false, columnDefinition = "text")
    private String type;
}
