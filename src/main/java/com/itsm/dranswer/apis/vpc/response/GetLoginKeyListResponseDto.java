package com.itsm.dranswer.apis.vpc.response;

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
import java.util.Map;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLoginKeyListResponseDto  extends ResponseError {

    private GetLoginKeyListResponse.GetLoginKeyListRawResponse getLoginKeyListResponse;
    private Map responseError;

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
