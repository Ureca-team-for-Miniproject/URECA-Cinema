package com.ureca.domain.dto;

import com.ureca.domain.entity.MembershipRankEntity;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberShipRankDTO {
    private Integer code;
    private String rankName;
    private String rank;
    private Double acmltRate;

    @Builder
    public MemberShipRankDTO(Integer code, String rankName, String rank, Double acmltRate) {
        this.code = code;
        this.rankName = rankName;
        this.rank = rank;
        this.acmltRate = acmltRate;
    }

    public MembershipRankEntity toEntity() {
        return MembershipRankEntity.builder()
                .code(code)
                .rankName(rankName)
                .rank(rank)
                .acmltRate(acmltRate)
                .build();
    }
}
