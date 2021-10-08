package com.itsm.dranswer.apis.vpc.dto;

/*
 * @package : com.itsm.dranswer.apis.vpc.dto
 * @name : AcgRuleRawResponse.java
 * @date : 2021-10-08 오전 11:16
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AcgRuleRawResponse {
    private String requestId;
    private String returnCode;
    private String returnMessage;
    private Integer totalRows;
    private List<AccessControlGroupRule> accessControlGroupRuleList;
}
