package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : NCloudSubnetEnv.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NCloudSubnetEnv extends BaseEntity implements Serializable  {

    private static final long serialVersionUID = 552625688797273929L;

    @Id
    @Column(columnDefinition = "varchar(36) COMMENT 'Subnet 번호'")
    private String subnetNo;

    @Column(columnDefinition = "varchar(30) COMMENT 'Subnet 이름'")
    private String subnetName;

    @Column(columnDefinition = "varchar(30) COMMENT '리전코드'")
    private String regionCode;

    @Column(columnDefinition = "varchar(30) COMMENT 'ZONE 코드'")
    private String zoneCode;

    @Column(columnDefinition = "varchar(30) COMMENT 'Network ACL 번호'")
    private String networkAclNo;

    @Column(columnDefinition = "varchar(30) COMMENT 'Subnet 이름'")
    private String subnetTypeCode; // PUBLIC | PRIVATE

    @Column(columnDefinition = "varchar(30) COMMENT 'Subnet 이름'")
    private String usageTypeCode;  // GEN


}
