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
import com.itsm.dranswer.config.CustomMailSender;
import com.itsm.dranswer.storage.UseStorageInfo;
import com.itsm.dranswer.users.UserInfo;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36) COMMENT '신청번호'")
    private String reqSeq;

    @Column(columnDefinition = "bigint COMMENT '신청회원번호'")
    private Long reqUserSeq;

    @Column(columnDefinition = "varchar(36) COMMENT 'Server Instance No.'")
    private String serverInstanceNo;

    @Column(columnDefinition = "varchar(36) COMMENT 'Server Name'")
    private String serverName;

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

    @Column(columnDefinition = "varchar(36) COMMENT '로그인키 이름'")
    private String loginKeyName;

    @Column(columnDefinition = "varchar(4000) COMMENT 'private key'")
    private String loginPrivateKey;

    @Column(columnDefinition = "int COMMENT '사용일수 0이면 무기한'")
    private Integer usingDays;

    @Column(columnDefinition = "datetime COMMENT '종료일시'")
    protected LocalDateTime endDate;

    @Column(columnDefinition = "varchar(4000) COMMENT '거절사유'")
    private String rejectReason;

    @Column(columnDefinition = "varchar(4000) COMMENT '취소사유'")
    private String cancelReason;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nCloudServerEnv")
    private List<UseStorageInfo> useStorageInfoList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nCloudServerEnv")
    private List<NCloudNetworkInterface> networkInterfaceList = new ArrayList<>();


    public NCloudServerEnv(NCloudServerEnvDto dto, UserInfo userInfo) {
        super();

        this.reqUserInfo = userInfo;
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

        return new NCloudServerEnvDto(this, false);
    }

    public void accept() {
        if(ApproveStatus.REQUEST == this.approveStatus){
            this.approveStatus = ApproveStatus.ACCEPT;

            if(this.usingDays > 0){
                this.endDate = LocalDateTime.now().plusDays(this.usingDays);
            }else{
                this.endDate = LocalDateTime.now().plusDays(9999);
            }
        }else{
            throw new IllegalArgumentException("처리 불가능한 상태 입니다");
        }
    }

    public void cancelAccept(){
        if(ApproveStatus.REQ_CANCEL == this.approveStatus){
            this.approveStatus = ApproveStatus.CANCEL;
        }else{
            throw new IllegalArgumentException("처리 불가능한 상태 입니다");
        }
    }

    public void created(String serverInstanceNo) {
        this.serverInstanceNo = serverInstanceNo;
       if(this.approveStatus != ApproveStatus.ACCEPT){
            throw new IllegalArgumentException("처리 불가능한 상태 입니다");
        }

        this.approveStatus = ApproveStatus.CREATED;
    }

    public void end() {

        if(this.approveStatus != ApproveStatus.CREATED && this.approveStatus != ApproveStatus.EXPIRED){
            throw new IllegalArgumentException("처리 불가능한 상태 입니다");
        }
        this.approveStatus = ApproveStatus.END;
    }

    public void reject(String rejectReason) {

        if(this.approveStatus != ApproveStatus.REQUEST){
            throw new IllegalArgumentException("처리 불가능한 상태 입니다");
        }
        this.approveStatus = ApproveStatus.REJECT;
        this.rejectReason = rejectReason;
    }

    public void update(String serverInstanceNo, String vpcNo, String subnetNo, String acgNo, String loginKeyName, String loginPrivateKey, String serverName) {

        this.created(serverInstanceNo);

        this.vpcNo = vpcNo;
        this.subnetNo = subnetNo;
        this.loginKeyName = loginKeyName;
        this.loginPrivateKey = loginPrivateKey;
        this.serverName = serverName;
    }

    public void checkApproved() {

        if(this.approveStatus != ApproveStatus.ACCEPT){
            throw new IllegalArgumentException("처리 불가능한 상태 입니다");
        }
    }

    public boolean checkUser(Long userSeq) {
        return this.reqUserSeq==userSeq;
    }

    public void cancel(String cancelReason) {

        if(ApproveStatus.REQUEST == this.approveStatus){
            this.approveStatus = ApproveStatus.CANCEL;
        }else if(ApproveStatus.ACCEPT == this.approveStatus){
            this.approveStatus = ApproveStatus.REQ_CANCEL;
            this.cancelReason = cancelReason;
        }else if(ApproveStatus.CREATED == this.approveStatus){
            this.approveStatus = ApproveStatus.REQ_CANCEL;
            this.cancelReason = cancelReason;
        }else{
            throw new IllegalArgumentException("처리 불가능한 상태 입니다");
        }
    }

    public void expired(CustomMailSender customMailSender) {
        this.approveStatus = ApproveStatus.EXPIRED;

        String email = "ask@thelaif.com";
        String mailSubject = "[닥터앤서] 학습 환경 만료 안내";
        String title = "학습 환경 만료 안내";
        String agencyName = this.getReqUserInfo().getAgencyInfo().getAgencyName();
        String serverName = this.serverName;
        String userName = this.getReqUserInfo().getUserName();

        String msg = userName + "(" + agencyName +") 님의 " + serverName + "이(가) 만료 되었습니다.";

        customMailSender.sendExpiredAlarmMail(email, mailSubject, title, msg);
    }

}
