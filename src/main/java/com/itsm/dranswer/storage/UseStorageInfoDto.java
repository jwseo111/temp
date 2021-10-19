package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : UseStorageInfoDto.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.AgencyInfo;
import com.itsm.dranswer.users.AgencyInfoDto;
import com.itsm.dranswer.users.UserInfo;
import com.itsm.dranswer.users.UserInfoDto;
import lombok.*;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UseStorageInfoDto extends BaseEntity {

    private String useStorageId;

    private UseStorageStat useStorageStatCode;

    private String cancelReason;

    private String rejectReason;

    private Integer usingDays;

    private String reqOpenId;

    private Long reqUserSeq;

    private OpenStorageInfoDto openStorageInfo;

    private UserInfoDto hUserInfo;

    private AgencyInfoDto agencyInfo;

    protected LocalDateTime acceptDate;

    protected LocalDateTime endDate;

    private UserInfoDto cUserInfo;

    private AgencyInfoDto cAgencyInfo;

    public UseStorageInfoDto(UseStorageInfo source) {

        copyProperties(source, this);
    }

    public UseStorageInfoDto(UseStorageInfo source, OpenStorageInfo openStorageInfo, UserInfo hUserInfo, AgencyInfo agencyInfo, UserInfo cUserInfo, AgencyInfo cAgencyInfo) {

        copyProperties(source, this);

        this.openStorageInfo = new OpenStorageInfoDto(openStorageInfo);
        this.hUserInfo = new UserInfoDto(hUserInfo);
        this.agencyInfo = new AgencyInfoDto(agencyInfo);
        this.cUserInfo = new UserInfoDto(cUserInfo);
        this.cAgencyInfo = new AgencyInfoDto(cAgencyInfo);
    }
}
