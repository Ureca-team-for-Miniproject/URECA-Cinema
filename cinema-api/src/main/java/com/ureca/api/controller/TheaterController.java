package com.ureca.api.controller;

import com.ureca.domain.entity.TheaterInfoEntity;
import com.ureca.domain.repository.TheaterInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TheaterController {

    @Autowired
    private TheaterInfoRepository theaterInfoRepository;

    @GetMapping("/test")
    @ResponseBody
    public void test() {

//상영관 테스트 데이터
        TheaterInfoEntity theaterInfo =
                TheaterInfoEntity.builder()
                        .theaterId("104")
                        .theaterNm("4관")
                        .theaterCd("S2")
                        .totalSeats(140)
                        .build();

        //theaterInfo 저장
        theaterInfoRepository.save(theaterInfo);

        System.out.println("===================================");

    }
}
