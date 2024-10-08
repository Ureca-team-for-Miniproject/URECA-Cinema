package com.ureca.domain.entity;

import com.ureca.domain.dto.MemberDTO;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "member")
@Getter
@ToString(exclude = {"code"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {
    @Id
    @Column(name = "id", length = 20, nullable = false)
    private String id;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "birth")
    @Temporal(TemporalType.DATE)
    private Date birth;

    @Column(name = "totalReservedTickets", nullable = false)
    @ColumnDefault("0")
    private Integer totalReservedTickets;

    @Column(name = "lastLoginTime")
    @Temporal(TemporalType.DATE)
    private Date lastLoginTime;

    @Column(name = "acmltCnt")
    @ColumnDefault("0")
    private Integer acmltCnt;

    @Column(name = "isInactive", nullable = false)
    @ColumnDefault("false")
    private boolean isInactive; // 추가된 필드

    @ManyToOne
    @JoinColumn(name = "code")
    private MembershipRankEntity code;

    // 빌더 패턴을 위한 생성자
    @Builder
    public MemberEntity(
            String id,
            String password,
            String name,
            String phone,
            Date birth,
            Integer totalReservedTickets,
            Date lastLoginTime,
            Integer acmltCnt,
            boolean isInactive,
            MembershipRankEntity code) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.totalReservedTickets =
                totalReservedTickets != null ? totalReservedTickets : 0; // 기본값 0 설정
        this.lastLoginTime = lastLoginTime;
        this.acmltCnt = acmltCnt != null ? acmltCnt : 0; // 기본값 0 설정
        this.isInactive = isInactive;
        this.code = code;
    }

    public MemberDTO toDTO() {
        return MemberDTO.builder()
                .id(id)
                .password(password)
                .name(name)
                .phone(phone)
                .birth(birth)
                .totalReservedTickets(totalReservedTickets)
                .lastLoginTime(lastLoginTime)
                .acmltCnt(acmltCnt)
                .code(code)
                .build();
    }
}
