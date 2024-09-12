package com.ureca.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "commonCode")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCodeEntity {

    @Id
    @Column(name = "codeId", length = 10, nullable = false)
    private String codeId;

    @Column(name = "codeNm", length = 20)
    private String codeNm;

    @Column(name = "codeDesc", length = 40)
    private String codeDesc;

    @Builder
    public CommonCodeEntity(String codeId, String codeNm, String codeDesc) {
        this.codeId = codeId;
        this.codeNm = codeNm;
        this.codeDesc = codeDesc;
    }
}
