package com.itsm.dranswer.apis.classic.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreatePublicIpInstance {

    private String serverInstanceNo;

    private String publicIpDescription;

    private String internetLineTypeCode;

    private String regionNo;

    private String zoneNo;
}
