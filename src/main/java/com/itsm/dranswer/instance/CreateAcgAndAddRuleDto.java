package com.itsm.dranswer.instance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.vpc.request.AddAcgRuleRequestDto;
import com.itsm.dranswer.apis.vpc.request.CreateAccessControlGroupRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateAcgAndAddRuleDto {

    CreateAccessControlGroupRequestDto createAccessControlGroupRequestDto;
    AddAcgRuleRequestDto addAcgInboundRuleRequestDto;
    AddAcgRuleRequestDto addAcgOutboundRuleRequestDto;

}
