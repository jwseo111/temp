package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : CreateAcgAndAddRuleDto.java
 * @date : 2021-10-08 오후 1:52
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
