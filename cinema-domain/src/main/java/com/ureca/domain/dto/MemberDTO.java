package com.ureca.domain.dto;

import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.entity.MembershipRankEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MemberDTO {
    private String id;
    private String password;
    private String name;
    private String phone;
    private Date birth;
    private Integer totalReservedTickets;
    private Date lastLoginTime;
    private Integer acmltCnt;
    private MemberShipRankDTO code;

    @Builder
    public MemberDTO(
            String id,
            String password,
            String name,
            String phone,
            Date birth,
            Integer totalReservedTickets,
            Date lastLoginTime,
            Integer acmltCnt,
            MembershipRankEntity code) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.totalReservedTickets = totalReservedTickets;
        this.lastLoginTime = lastLoginTime;
        this.acmltCnt = acmltCnt;
        this.code = code.toDTO();
    }

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .password(password)
                .name(name)
                .phone(phone)
                .birth(birth)
                .totalReservedTickets(totalReservedTickets)
                .lastLoginTime(lastLoginTime)
                .acmltCnt(acmltCnt)
                .code(code.toEntity())
                .build();
    }
}
