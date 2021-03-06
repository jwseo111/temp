package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : CreateNetworkInterfaceResponseDto.java
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
public class CreateNetworkInterfaceResponseDto  extends ResponseError {

    private CreateNetworkInterfaceRawResponseDto createNetworkInterfaceResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateNetworkInterfaceRawResponseDto {
        private String requestId;
        private String returnCode;
        private String returnMessage;
        private List<NetworkInterfaceDto> networkInterfaceList;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NetworkInterfaceDto {
        private String networkInterfaceNo;
        private String networkInterfaceName;
        private String subnetNo;
        private Boolean deleteOnTermination;
        private Boolean isDefault;
        private String deviceName;
        private CommonCode networkInterfaceStatus;
        private CommonCode instanceType;
        private CommonCode instanceNo;
        private String ip;
        private List<AccessControlGroupNoListDto> accessControlGroupNoList;
        private String networkInterfaceDescription;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccessControlGroupNoListDto {
        private String accessControlGroupNo;
    }
}
