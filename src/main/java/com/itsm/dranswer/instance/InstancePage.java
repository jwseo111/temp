package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.storage
 * @name : InstancePage.java
 * @date : 2021-06-23 오후 3:32
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstancePage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 학습환경관리> 학습환경 사용신청 목록
    @GetMapping(value = "/env/instance/main")
    public String envInstanceList() {
        return "pages/instance/envInstanceList";

    }
    // 학습환경관리> 학습환경 사용신청 상세
    @GetMapping(value = "/env/instance/view")
    public String envInstanceView() {
        return "pages/instance/envInstanceView";

    }
    // 학습환경관리> 학습환경 사용신청
    @GetMapping(value = "/env/instance/req")
    public String envInstanceReq() {
        return "pages/instance/envInstanceReq";

    }
    // 학습환경관리> 학습환경 사용신청
    @GetMapping(value = "/env/instance/reqS")
    public String envInstanceReqS() {
        return "pages/instance/envInstanceReqS";

    }


}
