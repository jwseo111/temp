package com.itsm.dranswer.instance;

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
public class NCloudVpcEnv extends BaseEntity implements Serializable  {

    private static final long serialVersionUID = 6674876818969083087L;

    @Id
    @Column(columnDefinition = "varchar(36) COMMENT 'VPC 번호'")
    private String vpcNo;

    @Column(columnDefinition = "varchar(30) COMMENT 'VPC 이름'")
    private String vpcName;

    @Column(columnDefinition = "varchar(100) COMMENT 'VPC IP 주소 범위'")
    private String ipv4CidrBlock;


}
