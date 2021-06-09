package com.itsm.dranswer.etc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileUploadPage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/etc/fileUploadTemplate")
    public String fileUploadTemplate() {

        return "pages/etc/basicUploadTemplate";
    }

}
