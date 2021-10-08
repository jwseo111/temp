package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : AgencyService.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AgencyService {

    private final AgencyInfoRepo agencyInfoRepo;

    @Autowired
    public AgencyService(AgencyInfoRepo agencyInfoRepo){
        this.agencyInfoRepo = agencyInfoRepo;
    }

    /**
     *
     * @methodName : getAgencyList
     * @date : 2021-10-08 오후 2:47
     * @author : xeroman.k
 * @param agencyTypeCode
 * @param agencyName
 * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.users.AgencyInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<AgencyInfoDto> getAgencyList(AgencyType agencyTypeCode, String agencyName, Pageable pageable){

        Page<AgencyInfo> pageAgency;

        if(agencyTypeCode == null){
            pageAgency = agencyInfoRepo.findByAgencyNameContains(agencyName, pageable);
        }else{
            pageAgency = agencyInfoRepo.findByAgencyTypeCodeAndAgencyNameContains(agencyTypeCode, agencyName, pageable);
        }

        return pageAgency.map(AgencyInfoDto::new);

    }

}
