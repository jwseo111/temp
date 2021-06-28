package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.commons.Disease;
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
public class ReqStorageInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2130907303474265124L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36) COMMENT '저장신청Id'")
    private String reqStorageId;

    @Column(columnDefinition = "varchar(100) COMMENT '저장데이터명'")
    private String dataName;

    @Column(columnDefinition = "varchar(20) COMMENT '질병코드'")
    @Enumerated(EnumType.STRING)
    private Disease diseaseCode;

    @Column(columnDefinition = "bigint COMMENT '질병책임자회원번호'")
    private Long diseaseManagerUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diseaseManagerUserSeq", referencedColumnName = "userSeq", insertable = false, updatable = false)
    private UserInfo diseaseManagerUserInfo;

    @Column(columnDefinition = "varchar(10) COMMENT '저장신청상태코드'")
    @Enumerated(EnumType.STRING)
    private ReqStorageStat reqStorageStatCode;

    @Column(columnDefinition = "varchar(100) COMMENT '저장소명'")
    private String bucketName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucketName", referencedColumnName = "bucketName", insertable = false, updatable = false)
    private BucketInfo bucketInfo;

    @Column(columnDefinition = "varchar(100) COMMENT '삭제사유'")
    private String deleteReason;

    @Column(columnDefinition = "varchar(100) COMMENT '거절사유'")
    private String rejectReason;

    public ReqStorageInfo(ReqStorageInfoDto reqStorageInfoDto) {
        this.dataName = reqStorageInfoDto.getDataName();
        this.diseaseCode = reqStorageInfoDto.getDiseaseCode();
        this.diseaseManagerUserSeq = reqStorageInfoDto.getDiseaseManagerUserSeq();
        this.reqStorageStatCode = ReqStorageStat.S_REQ;
    }

    public ReqStorageInfoDto convertDto() {
        return new ReqStorageInfoDto(this);
    }

    public boolean checkCreateUser(Long userSeq) {
        return this.getCreatedBy() == userSeq;
    }

    public void reqCancel() {
        if(this.reqStorageStatCode == ReqStorageStat.S_ACC){
            this.reqStorageStatCode = ReqStorageStat.D_REQ;
        }else{

        }


    }

    public void approve() {
        this.reqStorageStatCode = ReqStorageStat.S_ACC;
    }
}
