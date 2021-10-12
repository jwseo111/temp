package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : GetVpcServerDetailResponseDto.java
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
public class GetVpcServerDetailResponseDto extends ResponseError {
    private GetServerInstanceRawResponseDto getServerInstanceDetailResponse;

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
        private Integer cpuCount;
        private Long memorySize;
        private CommonCode platformType;
        private String loginKeyName;
        private String publicIpInstanceNo;
        private String publicIp;
        private CommonCode serverInstanceStatus;
        private CommonCode serverInstanceOperation;
        private String serverInstanceStatusName;
        private String serverImageProductCode;
        private String serverProductCode;
        private Boolean isProtectServerTermination;
        private String zoneCode;
        private String regionCode;
        private String vpcNo;
        private String subnetNo;
        private List<String> networkInterfaceNoList;
        private String initScriptNo;
        private CommonCode serverInstanceType;
        private CommonCode baseBlockStorageDiskType;
        private CommonCode baseBlockStorageDiskDetailType;
        private String placementGroupNo;
        private String placementGroupName;
        private String memberServerImageInstanceNo;
    }
}
