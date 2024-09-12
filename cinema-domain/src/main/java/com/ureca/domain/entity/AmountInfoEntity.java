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
@Table(name = "amountInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AmountInfoEntity {

    @Id
    @Column(name = "seatType", length = 4, nullable = false)
    private String seatType;

    @Column(name = "seatAmount")
    private Integer seatAmount;

    @Builder
    public AmountInfoEntity(String seatType, Integer seatAmount) {
        this.seatType = seatType;
        this.seatAmount = seatAmount;
    }
}
