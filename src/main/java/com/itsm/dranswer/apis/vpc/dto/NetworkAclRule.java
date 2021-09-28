package com.itsm.dranswer.apis.vpc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.classic.dto.CommonCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NetworkAclRule {

    private String networkAclNo;
    private Integer priority; // required > unique no
    private String protocolTypeCode ; // required > TCP or UDP or ICMP
    private CommonCode protocolType;
    private String ipBlock;
    private String denyAllowGroupNo;
    private String portRange;
    private CommonCode ruleAction;
    private String ruleActionCode ; // required > ALLOW or DROP
    private CommonCode networkAclRuleType;
    private String networkAclRuleDescription;

}
