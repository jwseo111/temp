package com.itsm.dranswer.apis.vpc.request;

/*
 * @package : com.itsm.dranswer.apis.vpc.request
 * @name : RemoveNetworkAclRuleRequestDto.java
 * @date : 2021-10-08 오후 1:08
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
public class RemoveNetworkAclRuleRequestDto {

    private String regionCode;
    private String networkAclNo;
    private List<NetworkAclRule> networkAclRuleList;

}
