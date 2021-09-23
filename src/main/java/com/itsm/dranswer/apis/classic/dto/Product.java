package com.itsm.dranswer.apis.classic.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String productCode;
    private String productName;
    private CommonCode productType;
    private String productDescription;
    private CommonCode infraResourceType;
    private CommonCode infraResourceDetailType;
    private Integer cpuCount;
    private Long memorySize;
    private Long baseBlockStorageSize;
    private CommonCode platformType;
    private String osInformation;
    private CommonCode diskType;
    private String dbKindCode;
    private Long addBlockStroageSize;
}
