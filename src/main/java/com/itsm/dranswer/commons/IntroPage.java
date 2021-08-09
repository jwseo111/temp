package com.itsm.dranswer.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroPage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 학습포탈소개 - 사용자가이드
    @GetMapping(value = "/intro/guide")
    public String introGuide() {

        return "pages/intro/guide";
    }

}
