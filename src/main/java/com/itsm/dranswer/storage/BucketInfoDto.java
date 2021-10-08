package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : BucketInfoDto.java
 * @date : 2021-10-08 오후 2:14
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import lombok.*;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BucketInfoDto {

    private String bucketName;

    private String bucketDesc;

    private Long bucketSize;

    private Long diseaseManagerUserSeq;

    public BucketInfoDto(BucketInfo bucketInfo) {
        copyProperties(bucketInfo, this);
    }
}
