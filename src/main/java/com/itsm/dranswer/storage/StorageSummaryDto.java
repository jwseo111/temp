package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : StorageSummaryDto.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.Disease;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StorageSummaryDto {

    private Disease diseaseCode;

    private Long bucketSize;

}
