package com.itsm.dranswer.storage;

import lombok.*;

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

}
