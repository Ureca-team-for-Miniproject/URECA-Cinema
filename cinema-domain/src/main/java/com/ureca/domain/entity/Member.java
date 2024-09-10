package com.ureca.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "member")
@Getter
@ToString(exclude = {"code"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Column(name = "id", length = 20, nullable = false)
    private String id;

    @Column(name = "password", length = 30)
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
    @ColumnDefault("0") // 데이터베이스 기본값 설정
    private Integer acmltCnt; // 자바 코드 기본값 설정

    @ManyToOne
    @JoinColumn(name = "code")
    private MembershipRank code;

    // 빌더 패턴을 위한 생성자
    @Builder
    public Member(String id, String password, String name, String phone, Date birth,
                  Integer totalReservedTickets, Date lastLoginTime, Integer acmltCnt, MembershipRank code) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.totalReservedTickets = totalReservedTickets != null ? totalReservedTickets : 0; // 기본값 0 설정
        this.lastLoginTime = lastLoginTime;
        this.acmltCnt = acmltCnt != null ? acmltCnt : 0; // 기본값 0 설정
        this.code = code;
    }
}

