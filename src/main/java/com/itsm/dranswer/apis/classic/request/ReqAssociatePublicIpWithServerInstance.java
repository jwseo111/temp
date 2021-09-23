package com.itsm.dranswer.apis.classic.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReqAssociatePublicIpWithServerInstance {

    private String publicIpInstanceNo;
    private String serverInstanceNo;
}
