package com.itsm.dranswer.instance;

import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NCloudNetworkInterfaceDto extends BaseEntity {

    private String interfaceId;

    private Integer networkInterfaceOrder;

    private String networkInterfaceNo;

    private Long reqSeq;

}
