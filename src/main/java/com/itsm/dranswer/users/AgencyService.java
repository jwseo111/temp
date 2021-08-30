package com.itsm.dranswer.users;

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
