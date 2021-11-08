package com.itsm.dranswer.apis.vpc.request;

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
public class DeleteAccessControlGroupRequestDto {

    private String regionCode;
    private String vpcNo;
    private String accessControlGroupNo;

}
