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
public class CreateNetworkInterfaceRequestDto {

    private String regionCode;
    private String vpcNo;
    private String subnetNo;
    private String networkInterfaceName;
    private List<String> accessControlGroupNoList;
    private String serverInstanceNo;
    private String ip;
    private List<String> secondaryIpList;
    private Integer secondaryIpCount;
    private String networkInterfaceDescription;

}
