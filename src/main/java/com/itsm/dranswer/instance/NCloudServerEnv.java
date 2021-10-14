package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : NCloudServerEnv.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.storage.UseStorageInfo;
import com.itsm.dranswer.users.UserInfo;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NCloudServerEnv extends BaseEntity implements Serializable  {

    private static final long serialVersionUID = 3153476030426250868L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint COMMENT '신청번호'")
    private Long reqSeq;

    @Column(columnDefinition = "bigint COMMENT '신청회원번호'")
    private Long reqUserSeq;

    @Column(columnDefinition = "varchar(36) COMMENT 'Server Instance No.'")
    private String serverInstanceNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqUserSeq", referencedColumnName = "userSeq", insertable = false, updatable = false)
    private UserInfo reqUserInfo;

    @Column(columnDefinition = "varchar(36) COMMENT 'VPC NO'")
    private String vpcNo;

    @Column(columnDefinition = "varchar(36) COMMENT 'SUBNET NO'")
    private String subnetNo;

    @Column(columnDefinition = "varchar(36) COMMENT '운영체제'")
    @Enumerated(EnumType.STRING)
    private OsImageType osImageType;

    @Column(columnDefinition = "varchar(36) COMMENT '서버 타입'")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(columnDefinition = "varchar(36) COMMENT '저장소 타입'")
    private String storageType;

    @Column(columnDefinition = "varchar(50) COMMENT '서버 스펙'")
    private String productCode;

    @Column(columnDefinition = "bit(1) COMMENT '공인IP 사용 여부'")
    private Boolean associateWithPublicIp;

    @Column(columnDefinition = "varchar(500) COMMENT '서버설명'")
    private String serverDescription;

    @Column(columnDefinition = "varchar(36) COMMENT '승인상태'")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatus;

    @Column(columnDefinition = "varchar(36) COMMENT '인증키 이름'")
    private String loginKeyName;

    @Column(columnDefinition = "int COMMENT '사용일수 0이면 무기한'")
    private Integer usingDays;

    @Column(columnDefinition = "varchar(36) COMMENT '사용신청Id'")
    private String useStorageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "useStorageId", referencedColumnName = "useStorageId", insertable = false, updatable = false)
    private UseStorageInfo useStorageInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nCloudServerEnv")
    private List<NCloudNetworkInterface> networkInterfaceList = new ArrayList<>();

    public NCloudServerEnv(NCloudServerEnvDto dto) {
        super();

        this.vpcNo = dto.getVpcNo();
        this.subnetNo = dto.getSubnetNo();
        this.reqUserSeq = dto.getReqUserSeq();
        this.osImageType = dto.getOsImageType();
        this.productType = dto.getProductType();
        this.storageType = dto.getStorageType();
        this.productCode = dto.getProductCode();
        this.associateWithPublicIp = dto.getAssociateWithPublicIp();
        this.serverDescription = dto.getServerDescription();
        this.approveStatus = ApproveStatus.REQUEST;
        this.loginKeyName = dto.getLoginKeyName();
        this.usingDays = dto.getUsingDays();

        for(NCloudNetworkInterfaceDto nCloudNetworkInterfaceDto : dto.getNetworkInterfaceList()){
            addNetworkInterfaceList(new NCloudNetworkInterface(nCloudNetworkInterfaceDto, this));
        }

    }

    public void addNetworkInterfaceList(NCloudNetworkInterface nCloudNetworkInterface){
        this.networkInterfaceList.add(nCloudNetworkInterface);
    }

    public NCloudServerEnvDto convertDto() {

        return new NCloudServerEnvDto(this);
    }

    public void accept() {
        this.approveStatus = ApproveStatus.ACCEPT;
    }

    public void created(String serverInstanceNo) {
        this.serverInstanceNo = serverInstanceNo;
        this.approveStatus = ApproveStatus.CREATED;
    }

    public void end() {
        this.approveStatus = ApproveStatus.END;
    }

    public void reject() {
        this.approveStatus = ApproveStatus.REJECT;
    }
}
