package com.itsm.dranswer.apis.classic.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReqGetServerProductList {
    private String serverImageProductCode;
    private String exclusionProductCode;
    private String productCode;
    private String generationCode="G1";
    private String regionNo = "1";
    private String zoneNo;
    private String internetLineTypeCode;
    private String cpuCount;
    private String memorySize;
    private String baseBlockStorageSize;
    private String productType;
    private String storageType="SSD";
}
