package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.UserInfo;
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
public class OpenStorageInfo extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = -269264650312946559L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36) COMMENT '공개신청Id'")
    private String reqOpenId;

    @Column(columnDefinition = "varchar(100) COMMENT '공개데이터설명'")
    private String openDataName;

    @Column(columnDefinition = "varchar(36) COMMENT '저장신청Id'")
    private String reqStorageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqStorageId", referencedColumnName = "reqStorageId", insertable = false, updatable = false)
    private ReqStorageInfo reqStorageInfo;

    @Column(columnDefinition = "bigint COMMENT '질병책임자회원번호'")
    private Long diseaseManagerUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diseaseManagerUserSeq", referencedColumnName = "userSeq", insertable = false, updatable = false)
    private UserInfo diseaseManagerUserInfo;

    @Column(columnDefinition = "varchar(10) COMMENT '공개신청상태코드'")
    @Enumerated(EnumType.STRING)
    private OpenStorageStat openStorageStatCode;

    @Column(columnDefinition = "varchar(100) COMMENT '취소사유'")
    private String cancelReason;

    @Column(columnDefinition = "varchar(100) COMMENT '거절사유'")
    private String rejectReason;

    public OpenStorageInfo(OpenStorageInfoDto openStorageInfoDto) {
        this.openDataName = openStorageInfoDto.getOpenDataName();
        this.reqStorageId = openStorageInfoDto.getReqStorageId();
        this.diseaseManagerUserSeq = openStorageInfoDto.getDiseaseManagerUserSeq();
        this.openStorageStatCode = OpenStorageStat.O_REQ;

    }

    public OpenStorageInfoDto convertDto() {
        return new OpenStorageInfoDto(this);
    }

    public void reqCancel(String cancelReason) {
        this.openStorageStatCode = OpenStorageStat.C_REQ;
        this.cancelReason = cancelReason;
    }

    public void approve() {

        if(this.openStorageStatCode == OpenStorageStat.O_REQ){
            this.openStorageStatCode = OpenStorageStat.O_ACC;
        }else if(this.openStorageStatCode == OpenStorageStat.C_REQ){
            this.openStorageStatCode = OpenStorageStat.C_ACC;
        }

    }

    public void reject(String rejectReason) {

        if(this.openStorageStatCode == OpenStorageStat.O_REQ){
            this.openStorageStatCode = OpenStorageStat.O_REJ;
        }else if(this.openStorageStatCode == OpenStorageStat.C_REQ){
            this.openStorageStatCode = OpenStorageStat.C_REJ;
        }

        this.rejectReason = rejectReason;

    }

    public boolean isCanceled(){
        return this.openStorageStatCode == OpenStorageStat.C_ACC;
    }

    public boolean isCancelRejected(){
        return this.openStorageStatCode == OpenStorageStat.C_REJ;
    }

    public boolean isApproved(){
        return this.openStorageStatCode == OpenStorageStat.O_ACC;
    }

    public boolean isRejected(){
        return this.openStorageStatCode == OpenStorageStat.O_REJ;
    }
}
