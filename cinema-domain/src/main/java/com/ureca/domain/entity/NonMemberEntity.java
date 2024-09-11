package com.ureca.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "nonMember")
@Getter
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NonMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    @Column(name = "serialNum")
    private Integer serialNum;

    @Column(name = "password", length = 30)
    private String password;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "birth")
    @Temporal(TemporalType.DATE)
    private Date birth;

    // 빌더 패턴 생성자
    @Builder
    public NonMemberEntity(String password, String name, String phone, Date birth) {
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
    }
}
