package com.itsm.dranswer.apis.vpc.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.ResponseError;
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
public class GetAccessControlGroupListResponseDto extends ResponseError {

    private GetAccessControlGroupListRawResponseDto getAccessControlGroupListResponse;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GetAccessControlGroupListRawResponseDto{
        private String requestId;
        private String returnCode;
        private String returnMessage;
        private List<CreateAccessControlGroupResponseDto.AcgInstanceDto> accessControlGroupList;
    }
}
