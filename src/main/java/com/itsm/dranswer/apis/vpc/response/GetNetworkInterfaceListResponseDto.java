package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : GetNetworkInterfaceListResponseDto.java
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
public class GetNetworkInterfaceListResponseDto  extends ResponseError {

    private GetNetworkInterfaceRawResponseDto getNetworkInterfaceListResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetNetworkInterfaceRawResponseDto {
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
        private String deleteOnTermination;
        private Boolean isDefault;
        private String deviceName;
        private CommonCode networkInterfaceStatus;
        private CommonCode instanceType;
        private String instanceNo;
        private String ip;
        private List<String> accessControlGroupNoList;
        private String networkInterfaceDescription;
    }
}
