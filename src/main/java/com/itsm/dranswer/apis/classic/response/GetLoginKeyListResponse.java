package com.itsm.dranswer.apis.classic.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetLoginKeyListResponse {

    private GetLoginKeyListRawResponse getLoginKeyListResponse;
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
        private List<LoginKey> loginKeyList = new ArrayList<>();
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
