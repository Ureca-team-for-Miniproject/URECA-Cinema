package com.ureca.domain.service;

import com.ureca.domain.dto.ReqSeatDTO;
import com.ureca.domain.dto.ResSeatDTO;
import com.ureca.domain.dto.SeatDTO;
import com.ureca.domain.repository.SeatInfoRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    private SeatInfoRepository seatInfoRepository;

    public SeatService(SeatInfoRepository seatInfoRepository) {
        this.seatInfoRepository = seatInfoRepository;
    }

    // 좌석 화면 정보 조회
    public ResSeatDTO getSeatInfo(ReqSeatDTO reqSeatDTO) {
        logger.info("reqSeatDTO { " + reqSeatDTO + " }");

        // 1. 단건 조회 - 상영 정보 조회
        ResSeatDTO resSeatDTO =
                seatInfoRepository.findSeatInfo(
                        reqSeatDTO.getUserId(),
                        reqSeatDTO.getScrnnId(),
                        reqSeatDTO.getMovieId(),
                        reqSeatDTO.getTheaterId());

        if (resSeatDTO != null) {
            // 2. 목록 조회 - 좌석 정보 조회
            List<SeatDTO> seatList = seatInfoRepository.findSeatList(reqSeatDTO.getScrnnId());
            resSeatDTO.setSeatList(seatList);
            resSeatDTO.setTotalSeatCnt(seatList.size());
            resSeatDTO.setColSeatCnt(12); // 열 길이 고정
            resSeatDTO.setRowSeatCnt(seatList.size() / 12);
        }

        logger.info("resSeatDTO { " + resSeatDTO + " }");
        return resSeatDTO;
    }
}
