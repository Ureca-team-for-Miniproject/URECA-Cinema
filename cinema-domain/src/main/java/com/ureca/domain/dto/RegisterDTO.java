package com.ureca.domain.dto;

import com.ureca.domain.entity.MemberEntity;
import com.ureca.domain.entity.MembershipRankEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
public class RegisterDTO {
    private String id;
    private String password;
    private String name;
    private String phone;
    private Date birth;
    private int code;

    @Builder
    public RegisterDTO(
            String id, String password, String name, String phone, Date birth, int code) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.code = code;
    }

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .password(password)
                .name(name)
                .phone(phone)
                .birth(birth)
                .code(
                        MembershipRankEntity.builder()
                                .code(1)
                                .rankName("브론즈")
                                .rank("BRONZE")
                                .acmltRate(0.00)
                                .build())
                .build();
    }
}
