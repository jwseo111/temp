package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : ReqUserDto.java
 * @date : 2021-10-08 오후 2:48
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.Disease;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReqUserDto {

    private Long userSeq;

    private String userName;

    private String agencyName;

    private String diseaseMainYn;

    private Disease diseaseCode;

    private String userPhoneNumber;

    private String userEmail;

    private Long managerUserSeq;

    private String managerUserName;

    public ReqUserDto(UserInfo userInfo) {

        AgencyInfo agencyInfo = userInfo.getAgencyInfo();

        this.userSeq = userInfo.getUserSeq();
        this.userName = userInfo.getUserName();
        this.agencyName = agencyInfo.getAgencyName();
        this.diseaseMainYn = agencyInfo.getAgencyTypeCode() == AgencyType.MNHP ? "Y":"N";
        this.diseaseCode = userInfo.getDiseaseCode();
        this.userPhoneNumber = userInfo.getUserPhoneNumber();
        this.userEmail = userInfo.getUserEmail();
        this.managerUserSeq = userInfo.getUserSeq();
        this.managerUserName = userInfo.getUserName();

    }

    public ReqUserDto(UserInfo userInfo, UserInfo manager) {

        AgencyInfo agencyInfo = manager.getAgencyInfo();

        this.userSeq = userInfo.getUserSeq();
        this.userName = userInfo.getUserName();
        this.agencyName = agencyInfo.getAgencyName();
        this.diseaseMainYn = agencyInfo.getAgencyTypeCode() == AgencyType.MNHP ? "Y":"N";
        this.diseaseCode = userInfo.getDiseaseCode();
        this.userPhoneNumber = userInfo.getUserPhoneNumber();
        this.userEmail = userInfo.getUserEmail();
        this.managerUserSeq = manager.getUserSeq();
        this.managerUserName = manager.getUserName();

    }
}
