package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : StoragePage.java
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
public class StoragePage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/lndata/store/main")
    public String storageList() {
        return "pages/storage/storageList";
    }

    @GetMapping(value = "/lndata/store/req")
    public String storageReq() {

        return "pages/storage/storageReq";
    }

}
