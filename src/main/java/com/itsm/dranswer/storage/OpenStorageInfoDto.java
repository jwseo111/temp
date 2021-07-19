package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.*;
import lombok.*;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OpenStorageInfoDto extends BaseEntity {

    private String reqOpenId;

    private String openDataName;

    private String reqStorageId;

    private ReqStorageInfoDto reqStorageInfo;

    private AgencyInfoDto agencyInfo;

    private Long diseaseManagerUserSeq;

    private UserInfoDto diseaseManagerUserInfo;

    private OpenStorageStat openStorageStatCode;

    private String cancelReason;

    private String rejectReason;

    private ReqUserDto reqUserDto;

    /**
     * use querydsl
     * @methodName : OpenStorageInfoDto
     * @date : 2021-07-06 오후 2:15
     * @author : xeroman.k
     * @param openStorageInfo
     * @param userInfo
     * @param agencyInfo
     * @return :
     * @throws
     * @modifyed :
     *
    **/
    public OpenStorageInfoDto(OpenStorageInfo openStorageInfo, UserInfo userInfo, AgencyInfo agencyInfo){
        copyProperties(openStorageInfo, this);
        this.diseaseManagerUserInfo = new UserInfoDto(userInfo);
        this.agencyInfo = new AgencyInfoDto(agencyInfo);
    }

    public OpenStorageInfoDto(OpenStorageInfo openStorageInfo) {
        copyProperties(openStorageInfo, this);
    }

    public OpenStorageInfoDto(OpenStorageInfo openStorageInfo, ReqStorageInfo reqStorageInfo, UserInfo userInfo, AgencyInfo agencyInfo, ReqUserDto reqUserDto){
        copyProperties(openStorageInfo, this);
        this.reqStorageInfo = new ReqStorageInfoDto(reqStorageInfo);
        this.diseaseManagerUserInfo = new UserInfoDto(userInfo);
        this.agencyInfo = new AgencyInfoDto(agencyInfo);
        this.reqUserDto = reqUserDto;
    }
}
