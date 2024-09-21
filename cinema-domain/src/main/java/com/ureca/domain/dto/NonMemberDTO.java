package com.ureca.domain.dto;

import com.ureca.domain.entity.NonMemberEntity;
import java.util.Date;
import lombok.*;

@Data
public class NonMemberDTO {
    private String id;
    private String password;
    private String name;
    private String phone;
    private Date birth;

    @Builder
    public NonMemberDTO(String id, String password, String name, String phone, Date birth) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
    }

    public NonMemberEntity toEntity() {
        return NonMemberEntity.builder()
                .password(password)
                .name(name)
                .phone(phone)
                .birth(birth)
                .build();
    }
}
