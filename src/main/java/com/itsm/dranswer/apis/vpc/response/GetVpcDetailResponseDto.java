package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : GetVpcDetailResponseDto.java
 * @date : 2021-10-08 오후 1:09
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.ResponseError;
import com.itsm.dranswer.apis.classic.dto.CommonCode;
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
public class GetVpcDetailResponseDto  extends ResponseError {
    private GetVpcDetailRawResponseDto getVpcDetailResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetVpcDetailRawResponseDto {
        private List<VpcInstanceDto> vpcList;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VpcInstanceDto {
        private String vpcNo;
        private String vpcName;
        private String ipv4CidrBlock;
        private String regionCode;
        private String createDate;
        private CommonCode vpcStatus;
    }
}
