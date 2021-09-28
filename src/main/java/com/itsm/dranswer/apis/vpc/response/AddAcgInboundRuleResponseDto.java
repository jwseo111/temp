package com.itsm.dranswer.apis.vpc.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.ResponseError;
import com.itsm.dranswer.apis.vpc.dto.AcgRuleRawResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddAcgInboundRuleResponseDto extends ResponseError {

    private AcgRuleRawResponse addAccessControlGroupInboundRuleResponse;

}
