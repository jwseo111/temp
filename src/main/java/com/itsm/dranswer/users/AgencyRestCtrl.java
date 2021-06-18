package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : AgencyRestCtrl.java
 * @date : 2021-06-17 오후 2:54
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


import com.itsm.dranswer.utils.ApiUtils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class AgencyRestCtrl {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AgencyService agencyService;

    @Autowired
    public AgencyRestCtrl(AgencyService agencyService){
        this.agencyService = agencyService;
    }

    @GetMapping(value = "/agency/list")
    public ApiResult<Page> getAgencyList(@RequestParam(required = false) AgencyType agencyTypeCode,
                                         @RequestParam(required = false, defaultValue = "") String agencyName,
                                         Pageable pageable){

        Page<AgencyInfoDto> pageAgency = agencyService.getAgencyList(agencyTypeCode, agencyName, pageable);

        return success(pageAgency);
    }

}
