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
public class VpcPublicIpInstanceResponseDto extends ResponseError {

    private VpcPublicIpInstanceRawResponseDto disassociatePublicIpFromServerInstanceResponse;
    private VpcPublicIpInstanceRawResponseDto deletePublicIpInstanceResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VpcPublicIpInstanceRawResponseDto {
        private List<PublicIpInstanceDto> publicIpInstanceList ;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicIpInstanceDto {
        private String publicIpInstanceNo;
        private String publicIp;
        private String publicIpDescription;
        private String publicIpInstanceStatusName;
        private CommonCode publicIpInstanceStatus;
        private String serverInstanceNo;
        private String serverName;
        private String privateIp;
        private CommonCode publicIpInstanceOperation;
    }
}
