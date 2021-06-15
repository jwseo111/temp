package com.itsm.dranswer.commons;

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
public class MainPage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/")
    public String main() {

        return "pages/common/main";
    }

}
