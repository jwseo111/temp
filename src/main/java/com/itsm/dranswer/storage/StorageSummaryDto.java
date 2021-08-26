package com.itsm.dranswer.storage;

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
