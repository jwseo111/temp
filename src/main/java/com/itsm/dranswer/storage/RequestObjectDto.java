package com.itsm.dranswer.storage;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestObjectDto {

    private String bucketName;
    private String objectName;
}
