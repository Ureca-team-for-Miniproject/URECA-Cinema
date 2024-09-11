package com.ureca.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "membershipRank")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MembershipRankEntity {

    @Id
    @Column(name = "code")
    private Integer code;
    
    @Column(name = "rank", length = 20)
    private String rank;

    @Column(name = "acmltRate")
    private Double acmltRate;

    // 빌더 패턴 생성자
    @Builder
    public MembershipRankEntity(Integer code, String rank, Double acmltRate) {
        this.code = code;
        this.rank = rank;
        this.acmltRate = acmltRate;
    }
}