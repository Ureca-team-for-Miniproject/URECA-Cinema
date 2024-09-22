package com.ureca.domain.entity;

import com.ureca.domain.dto.MemberShipRankDTO;
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

    @Column(name = "rankName", length = 20)
    private String rankName;

    @Column(name = "acmltRate")
    private Double acmltRate;

    @Column(name = "rank", length = 20)
    private String rank;

    // 빌더 패턴 생성자
    @Builder
    public MembershipRankEntity(Integer code, String rank, Double acmltRate, String rankName) {
        this.code = code;
        this.rank = rank;
        this.rankName = rankName;
        this.acmltRate = acmltRate;
    }

    public MemberShipRankDTO toDTO() {
        return MemberShipRankDTO.builder()
                .code(code)
                .rankName(rankName)
                .rank(rank)
                .acmltRate(acmltRate)
                .build();
    }
}
