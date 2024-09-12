package com.ureca.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theaterInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TheaterInfoEntity {

    @Id
    @Column(name = "theaterId", length = 20, nullable = false)
    private String theaterId;

    @Column(name = "theaterNm", length = 100)
    private String theaterNm;

    @Column(name = "theaterCd", length = 10)
    private String theaterCd;

    @Column(name = "totalSeats")
    private int totalSeats;

    @Builder
    public TheaterInfoEntity(String theaterId, String theaterNm, String theaterCd, int totalSeats) {
        this.theaterId = theaterId;
        this.theaterNm = theaterNm;
        this.theaterCd = theaterCd;
        this.totalSeats = totalSeats;
    }
}
