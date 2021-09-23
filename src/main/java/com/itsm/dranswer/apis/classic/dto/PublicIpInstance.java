package com.itsm.dranswer.apis.classic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicIpInstance {

    private String publicIpInstanceNo;
    private String publiclIp;
    private String publicIpDescription;
    private Date createDate;
    private CommonCode internetLineType;
    private String publicIpInstanceStatusName;
    private CommonCode publicIpInstanceStatus;
    private CommonCode publicIpInstanceOperation;
    private CommonCode publicIpKindType;
//        private ServerInstance serverInstanceAssociatedWithPublicIp;
//        private Region region;
//        private Zone zone;
}
