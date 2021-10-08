package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : GetLoginKeyListResponseDto.java
 * @date : 2021-10-08 오후 1:08
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itsm.dranswer.apis.ResponseError;
import com.itsm.dranswer.apis.classic.response.GetLoginKeyListResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLoginKeyListResponseDto  extends ResponseError {

    private GetLoginKeyListResponse.GetLoginKeyListRawResponse getLoginKeyListResponse;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetLoginKeyListRawResponse {
        private String requestId;
        private String returnCode;
        private String returnMessage;
        private Integer totalRows;
        private List<GetLoginKeyListResponse.LoginKey> loginKeyList = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginKey {
        private String fingerprint;
        private String keyName;
        private Date createDate;
    }
}
