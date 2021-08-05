package com.itsm.dranswer.commons;



/*
 * @package : com.itsm.dranswer.commons
 * @name : FooterPage.java
 * @date : 2021-08-03 오전 10:10
 * @author : itsmart
 * @version : 1.0.0
 * @modifyed :
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FooterPage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 이용약관
    @GetMapping(value = "/common/term")
    public String cmmTerm() {

        return "pages/common/cmmTerm";
    }

    // 개인정보처리방침
    @GetMapping(value = "/common/privatePolicy")
    public String cmmPrivatePolicy() {

        return "pages/common/cmmPrivatePolicy";
    }


    @GetMapping(value = "/mail/{mail}")
    public String mailAccept(@PathVariable String mail, Model model) {

        model.addAttribute("userName","이름");
        model.addAttribute("title","제목");

        model.addAttribute("certNumber", "123456");
        model.addAttribute("subject","요청명");
        model.addAttribute("reject","사유");

        return "mail/"+mail;
    }


}
