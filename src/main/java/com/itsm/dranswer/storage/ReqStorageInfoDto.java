package com.itsm.dranswer.storage;


/*
 * @package : com.itsm.dranswer.storage
 * @name : ReqStorageInfoDto.java
 * @date : 2021-06-24 오후 2:44
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.commons.Disease;
import com.itsm.dranswer.users.AgencyInfo;
import com.itsm.dranswer.users.AgencyInfoDto;
import com.itsm.dranswer.users.UserInfo;
import com.itsm.dranswer.users.UserInfoDto;
import lombok.*;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReqStorageInfoDto extends BaseEntity {

    private String reqStorageId;

    private String dataName;

    private Disease diseaseCode;

    private Long diseaseManagerUserSeq;

    private UserInfoDto diseaseManagerUserInfo;

    private AgencyInfoDto agencyInfo;

    private ReqStorageStat reqStorageStatCode;

    private String bucketName;

    private BucketInfo bucketInfo;

    private String deleteReason;

    private String rejectReason;

    public ReqStorageInfoDto(ReqStorageInfo reqStorageInfo, UserInfo userInfo, AgencyInfo agencyInfo){
        copyProperties(reqStorageInfo, this);
        this.diseaseManagerUserInfo = new UserInfoDto(userInfo);
        this.agencyInfo = new AgencyInfoDto(agencyInfo);
    }
}
