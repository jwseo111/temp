package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.commons.Disease;
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

    private ReqStorageStat reqStorageStatCode;

    private String bucketName;

    private BucketInfo bucketInfo;

    private String deleteReason;

    private String rejectReason;

    public ReqStorageInfoDto(ReqStorageInfo reqStorageInfo, UserInfo userInfo){
        copyProperties(reqStorageInfo, this);
        this.diseaseManagerUserInfo = new UserInfoDto(userInfo);
    }
}
