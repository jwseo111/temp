package com.itsm.dranswer.apis.vpc.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class GetVpcListRequestDto {
    private String regionCode;
    private String vpcStatusCode;
    private String vpcName;
    private List<String> vpcNoList;
}