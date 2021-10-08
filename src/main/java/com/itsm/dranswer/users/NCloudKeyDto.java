package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : NCloudKeyDto.java
 * @date : 2021-10-08 오후 2:48
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
