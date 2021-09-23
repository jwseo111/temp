package com.itsm.dranswer.apis.vpc.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.classic.dto.CommonCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateNetworkInterfaceResponseDto {

    private CreateNetworkInterfaceRawResponseDto createNetworkInterfaceResponse;
    private Map responseError;

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
        private CommonCode ip;
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
