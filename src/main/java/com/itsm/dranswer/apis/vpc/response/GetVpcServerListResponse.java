package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : GetVpcServerListResponse.java
 * @date : 2021-10-08 오후 1:09
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.ResponseError;
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
public class GetVpcServerListResponse  extends ResponseError {
    private GetServerInstanceRawResponseDto getServerInstanceListResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetServerInstanceRawResponseDto {
        private List<ServerInstanceDto> serverInstanceList;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServerInstanceDto {
        private String serverInstanceNo;
        private String serverName;
        private String serverDescription;
        private String cpuCount;
        private String memorySize;
        private String serverInstanceStatusName;
        private String regionCode;
        private String zoneCode;
        private String vpcNo;
        private String subnetNo;
    }
}
