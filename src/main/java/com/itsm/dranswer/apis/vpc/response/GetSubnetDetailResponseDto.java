package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : GetSubnetDetailResponseDto.java
 * @date : 2021-10-08 오후 1:08
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
public class GetSubnetDetailResponseDto  extends ResponseError {
    private GetSubnetDetailRawResponseDto getSubnetDetailResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetSubnetDetailRawResponseDto {
        private List<SubnetInstanceDto> subnetList;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubnetInstanceDto {
        private String subnetNo;
        private String subnetName;
        private String subnet;
        private CommonCode subnetStatus;
    }
}
