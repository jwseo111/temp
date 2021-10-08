package com.itsm.dranswer.apis.vpc.request;

/*
 * @package : com.itsm.dranswer.apis.vpc.request
 * @name : CreateSubnetRequestDto.java
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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateSubnetRequestDto {
    private String regionCode;
    private String zoneCode;
    private String vpcNo;
    private String subnetName;
    private String subnet;
    private String networkAclNo;
    private String subnetTypeCode = "PUBLIC"; // PUBLIC | PRIVATE
    private String usageTypeCode;  // GEN
}
