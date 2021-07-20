package com.itsm.dranswer.storage;

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

    private Long diseaseManagerUserSeq;

    public BucketInfoDto(BucketInfo bucketInfo) {
        copyProperties(bucketInfo, this);
    }
}
