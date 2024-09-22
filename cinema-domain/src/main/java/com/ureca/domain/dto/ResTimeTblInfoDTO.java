package com.ureca.domain.dto;

import com.ureca.domain.entity.ScreenInfoEntity;
import java.text.SimpleDateFormat;
import lombok.Data;

@Data
public class ResTimeTblInfoDTO {
    // 상영아이디
    private String scrnnId;
    // 상영시간
    private String startScrnn;
    private String endScrnn;
    // 좌석수
    private String totalSeats;
    private String avlblSeats;
    // 상영관정보
    private String theaterId;
    private String theaterNm;
    // 상영방식
    private String theaterCd;

    // ScreenInfoEntity to ResTimeTblInfoDTO
    public static ResTimeTblInfoDTO toResTimeTblInfoDTO(ScreenInfoEntity entity) {
        ResTimeTblInfoDTO dto = new ResTimeTblInfoDTO();
        dto.setScrnnId(entity.getScrnnId());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        dto.setStartScrnn(entity.getStartScrnn() != null ? sdf.format(entity.getStartScrnn()) : "");
        dto.setEndScrnn(entity.getEndScrnn() != null ? sdf.format(entity.getEndScrnn()) : "");
        dto.setTotalSeats("120");
        dto.setAvlblSeats(String.valueOf(entity.getAvlblSeats()));
        // ScreenEntity (entity.getTheaterId) -> TheaterEntity 객체 (.getTheaterNm) -> 영화관 이름
        dto.setTheaterId(entity.getTheaterId() != null ? entity.getTheaterId().getTheaterId() : "");
        dto.setTheaterNm(entity.getTheaterId() != null ? entity.getTheaterId().getTheaterNm() : "");
        dto.setTheaterCd(entity.getScrnnId().substring(0, 2));
        return dto;
    }
}
