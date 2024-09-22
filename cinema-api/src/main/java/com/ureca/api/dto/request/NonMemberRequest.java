package com.ureca.api.dto.request;

import com.ureca.domain.dto.NonMemberDTO;
import jakarta.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import lombok.Builder;
import lombok.Data;

@Data
public class NonMemberRequest {
    @NotBlank private String name;
    @NotBlank private String password;
    @NotBlank private String phone;
    @NotBlank private String birth;

    @Builder
    public NonMemberRequest(String name, String password, String phone, String birth) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.birth = birth;
    }

    public NonMemberDTO toDto() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return NonMemberDTO.builder()
                .name(name)
                .password(password)
                .phone(phone)
                .birth(formatter.parse(birth))
                .build();
    }
}
