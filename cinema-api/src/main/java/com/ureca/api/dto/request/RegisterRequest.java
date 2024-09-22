package com.ureca.api.dto.request;

import com.ureca.domain.dto.RegisterDTO;
import jakarta.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import lombok.Builder;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String email;
    @NotBlank private String password;
    @NotBlank private String name;
    @NotBlank private String phone;
    @NotBlank private String birth;

    @Builder
    public RegisterRequest(String email, String password, String name, String phone, String birth) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
    }

    public RegisterDTO toDto() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return RegisterDTO.builder()
                .id(email)
                .password(password)
                .name(name)
                .phone(phone)
                .birth(formatter.parse(birth))
                .build();
    }
}
