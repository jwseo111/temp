package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : RequestObjectDto.java
 * @date : 2021-10-08 오후 2:15
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
