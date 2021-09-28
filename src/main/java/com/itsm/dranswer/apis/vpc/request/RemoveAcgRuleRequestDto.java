package com.itsm.dranswer.apis.vpc.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.vpc.dto.AccessControlGroupRule;
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
public class RemoveAcgRuleRequestDto {

    private String regionCode;
    private String vpcNo;
    private String accessControlGroupNo;

    private List<AccessControlGroupRule> accessControlGroupRuleList;
}
