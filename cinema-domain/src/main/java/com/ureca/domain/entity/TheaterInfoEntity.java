package com.ureca.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 상영관 정보
@Entity
@Table(name = "theaterInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TheaterInfoEntity {

    // 상영관 아이디 (1층 3관:103)
    @Id
    @Column(name = "theaterId", length = 20, nullable = false)
    private String theaterId;

    // 상영관명 (1관, 5관, 4DX관)
    @Column(name = "theaterNm", length = 100)
    private String theaterNm;

    // 상영 방식 (공통코드 (S2: 2D))
    @Column(name = "theaterCd", length = 10)
    private String theaterCd;

    // 전체 좌석 수
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
