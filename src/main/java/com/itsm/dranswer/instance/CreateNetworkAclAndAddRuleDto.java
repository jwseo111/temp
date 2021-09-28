package com.itsm.dranswer.instance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.vpc.request.AddNetworkAclRuleRequestDto;
import com.itsm.dranswer.apis.vpc.request.CreateNetworkAclRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateNetworkAclAndAddRuleDto {

    CreateNetworkAclRequestDto createNetworkAclRequestDto;
    AddNetworkAclRuleRequestDto addNetworkAclInboundRuleRequestDto;
    AddNetworkAclRuleRequestDto addNetworkAclOutboundRuleRequestDto;

}
