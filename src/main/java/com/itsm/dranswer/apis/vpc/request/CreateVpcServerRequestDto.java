package com.itsm.dranswer.apis.vpc.request;

/*
 * @package : com.itsm.dranswer.apis.vpc.request
 * @name : CreateVpcServerRequestDto.java
 * @date : 2021-10-08 오후 1:07
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateVpcServerRequestDto {
    private String regionCode;
    private String vpcNo;
    private String subnetNo;
    private String serverName;
    private String serverProductCode;
    private String serverImageProductCode;
    private String memberServerImageInstanceNo;
    private Integer serverCreateCount;
    private String serverDescription;
    private String loginKeyName;
    private String feeSystemTypeCode = "MTRAT";
    private Boolean associateWithPublicIp;
    private List<NetworkInterface> networkInterfaceList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NetworkInterface {
        private Integer networkInterfaceOrder;
        private String networkInterfaceNo;
        private List<String> accessControlGroupNoList;
    }
}
