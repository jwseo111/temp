package com.itsm.dranswer.instance;

import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NCloudNetworkInterface extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2539913640482624932L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36) COMMENT 'interfaceId'")
    private String interfaceId;

    @Column(columnDefinition = "int COMMENT '인터페이스 순서'")
    private Integer networkInterfaceOrder;

    @Column(columnDefinition = "varchar(36) COMMENT 'networkInterface No'")
    private String networkInterfaceNo;

    @Column(columnDefinition = "bigint COMMENT '신청번호'")
    private Long reqSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqSeq", referencedColumnName = "reqSeq", insertable = false, updatable = false)
    private NCloudServerEnv nCloudServerEnv;

    public NCloudNetworkInterface (NCloudNetworkInterfaceDto dto){
        super();

        this.networkInterfaceOrder = dto.getNetworkInterfaceOrder();
        this.networkInterfaceNo = dto.getNetworkInterfaceNo();
    }
}
