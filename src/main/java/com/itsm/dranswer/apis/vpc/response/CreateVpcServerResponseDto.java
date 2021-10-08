package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : CreateVpcServerResponseDto.java
 * @date : 2021-10-08 오후 1:08
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
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateVpcServerResponseDto  extends ResponseError {
    private CreateServerInstanceRawResponseDto createServerInstancesResponse;
    private Map responseError;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateServerInstanceRawResponseDto {
        private String requestId;
        private String returnCode;
        private String returnMessage;
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
        private String createDate;
    }

}
