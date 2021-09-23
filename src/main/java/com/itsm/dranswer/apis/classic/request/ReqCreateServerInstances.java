package com.itsm.dranswer.apis.classic.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateServerInstances {

    private String serverImageProductCode;
    private String serverProductCode;
    private String memberServerImageNo;
    private String serverName;
    private String serverDescription;
    private String loginKeyName;
    private Boolean isProtectServerTermination = false;
    private Integer serverCreateCount;
    private Integer serverCreateStartNo;
    private String internetLineTypeCode;
    private String feeSystemTypeCode; // FXSUM(정액) or MTRAT(시간)
    private String zoneNo;
    private List<String> accessControlGroupConfigurationNoList = new ArrayList<>();
    private String userData;
    private String initScriptNo;
    private String raidTypeName;

}
