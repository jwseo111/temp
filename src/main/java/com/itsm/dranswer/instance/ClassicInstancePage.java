package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : ClassicInstancePage.java
 * @date : 2021-10-08 오후 1:52
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClassicInstancePage {

    @GetMapping(value = "/my/management/instance/classic/server/createServer")
    public String createServer() {

        return "pages/instance/createServer";
    }
}
