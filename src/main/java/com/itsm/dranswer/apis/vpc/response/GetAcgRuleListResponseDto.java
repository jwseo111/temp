package com.itsm.dranswer.apis.vpc.response;

/*
 * @package : com.itsm.dranswer.apis.vpc.response
 * @name : GetAcgRuleListResponseDto.java
 * @date : 2021-10-08 오후 1:08
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.apis.ResponseError;
import com.itsm.dranswer.apis.vpc.dto.AccessControlGroupRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAcgRuleListResponseDto extends ResponseError {

    private GetAcgRuleListRawResponseDto getAccessControlGroupRuleListResponse;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GetAcgRuleListRawResponseDto{
        private Integer totalRows;
        private List<AccessControlGroupRule> accessControlGroupRuleList = new ArrayList<>();
    }

}
