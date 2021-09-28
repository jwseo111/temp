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
public class AccessControlGroupRule {
    private String accessControlGroupNo;
    private String protocolTypeCode;
    private CommonCode protocolType;
    private String ipBlock;
    private String accessControlGroupSequence;
    private String portRange;
    private CommonCode accessControlGroupRuleType;
    private String accessControlGroupRuleDescription;
}
