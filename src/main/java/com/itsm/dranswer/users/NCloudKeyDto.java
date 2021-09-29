package com.itsm.dranswer.users;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NCloudKeyDto {

    private Long userSeq;

    private String nCloudObjStorageId;

    private String nCloudAccessKey;

    private String nCloudSecretKey;

    public NCloudKeyDto(UserInfo userInfo){
        this.userSeq = userInfo.getUserSeq();
        this.nCloudObjStorageId = userInfo.getNCloudObjStorageId();
        this.nCloudAccessKey = userInfo.getNCloudAccessKey();
        this.nCloudSecretKey = userInfo.getNCloudSecretKey();
    }
}
