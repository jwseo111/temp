package com.itsm.dranswer.apis.vpc.request;

/*
 * @package : com.itsm.dranswer.apis.vpc.request
 * @name : AddNetworkAclRuleRequestDto.java
 * @date : 2021-10-08 오전 11:24
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.vpc.dto.NetworkAclRule;
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
public class AddNetworkAclRuleRequestDto {

    private String regionCode;
    private String networkAclNo;
    private List<NetworkAclRule> networkAclRuleList;

}
