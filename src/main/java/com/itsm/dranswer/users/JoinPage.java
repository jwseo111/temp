package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : JoinPage.java
 * @date : 2021-06-23 오후 3:36
 * @author : itsmart
 * @version : 1.0.0
 * @modifyed :
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class JoinPage {

    private final Logger log = LoggerFactory.getLogger(getClass());

     // 회원가입 step1 (회원약관)
    @GetMapping(value = "/signup")
    public String signup() {

        return "pages/user/joinStep1";
    }

    // 회원가입 step2 (회원정보입력)
    @RequestMapping(value = "/regUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public String regUserInfo(JoinDto joinDto, Model model) {

        log.debug("joinDto {}", joinDto);
        joinDto.setNext("Y");
        model.addAttribute("join", joinDto);
        return "pages/user/joinStep2";
    }

     // 회원가입 complete (가입완료)
    @GetMapping(value = "/joinComplete")
    public String joinComplete() {

        return "pages/user/joinComplete";
    }


}
