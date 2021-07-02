package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserPage.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/login")
    public String login() {

        return "pages/user/login";
    }

    @GetMapping(value = "/my/management/storagepage")
    public String mypage() {
        //return "pages/user/mypage";
        return "pages/user/myStorageList";
    }

    // 마이페이지 > 질환데이터저장신청 목록
    @GetMapping(value = "/my/store/list")
    public String myStorageList() {

        return "pages/user/myStorageList";
    }

    // 마이페이지 > 질환데이터저장신청 상세
    @GetMapping(value = "/my/store/req")
    public String myStorageReq() {

        return "pages/user/myStorageReq";
    }
}
