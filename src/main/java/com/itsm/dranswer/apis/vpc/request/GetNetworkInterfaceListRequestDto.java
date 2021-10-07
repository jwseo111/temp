package com.itsm.dranswer.apis.vpc.request;

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
public class GetNetworkInterfaceListRequestDto {

    private String regionCode;
    private String subnetName;
    private List<String> networkInterfaceNoList;
    private String networkInterfaceName;
    private String networkInterfaceStatusCode = "NOTUSED";
    private String ip;
    private List<String> secondaryIpList;
    private String instanceNo;
    private Boolean isDefault;
    private String deviceName;
    private String serverName;
    private Integer pageNo;
    private Integer pageSize = 100;
}
