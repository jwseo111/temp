package com.itsm.dranswer.apis.vpc.request;

/*
 * @package : com.itsm.dranswer.apis.vpc.request
 * @name : GetNetworkInterfaceListRequestDto.java
 * @date : 2021-10-08 오후 1:07
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
    private String networkInterfaceStatusCode;// = "NOTUSED";
    private String ip;
    private List<String> secondaryIpList;
    private String instanceNo;
    private Boolean isDefault;
    private String deviceName;
    private String serverName;
    private Integer pageNo;
    private Integer pageSize = 100;
}
