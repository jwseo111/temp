package com.itsm.dranswer.apis.vpc.request;

/*
 * @package : com.itsm.dranswer.apis.vpc.request
 * @name : CreateNetworkInterfaceRequestDto.java
 * @date : 2021-10-08 오후 1:06
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
