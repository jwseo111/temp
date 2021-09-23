package com.itsm.dranswer.apis.classic.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.classic.dto.PublicIpInstance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePublicIpInstanceResponse {

    private CreatePublicIpInstanceRawResponse createPublicIpInstanceResponse;
    private Map responseError;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreatePublicIpInstanceRawResponse {
        private Integer totalRows;
        private List<PublicIpInstance> publicIpInstanceList = new ArrayList<PublicIpInstance>();
    }
}
