package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : NCloudNetworkInterfaceDto.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;

import static org.springframework.beans.BeanUtils.copyProperties;

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

    public NCloudNetworkInterfaceDto ( NCloudNetworkInterface source){
        copyProperties(source, this);
    }

}
