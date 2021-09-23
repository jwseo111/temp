package com.itsm.dranswer.apis.vpc.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.instance.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetServerProductListRequestDto {
    private String regionCode;
    private String zoneCode;
    private String serverImageProductCode;
    private String exclusionProductCode;
    private String productCode;
    private String generationCode; // G1 | G2


    private Integer cpuCount;
    private Integer memorySize;
    private Integer baseBlockStorageSize;
    private ProductType productType;
    private String storageType="SSD";
}
