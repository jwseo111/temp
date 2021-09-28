package com.itsm.dranswer.apis.vpc.response;

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
public class CreateNetworkAclResponseDto extends ResponseError {
    private CreateNetworkAclRawResponseDto createNetworkAclResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateNetworkAclRawResponseDto {
        private List<NetworkAclInstanceDto> networkAclList;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NetworkAclInstanceDto {
        private String networkAclNo;
        private String networkAclName;
        private String vpcNo;
        private CommonCode networkAclStatus;
        private String networkAclDescription;
        private Boolean isDefault;
    }
}
