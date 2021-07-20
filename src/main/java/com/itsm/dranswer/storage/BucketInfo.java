package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.BaseEntity;
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
public class BucketInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2184846481621621528L;

    @Id
    @Column(columnDefinition = "varchar(100) COMMENT '저장소명'")
    private String bucketName;

    @Column(columnDefinition = "varchar(100) COMMENT '저장소설명'")
    private String bucketDesc;

    @Column(columnDefinition = "bigint COMMENT '질병책임자회원번호'")
    private Long diseaseManagerUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diseaseManagerUserSeq", referencedColumnName = "userSeq", insertable = false, updatable = false)
    private UserInfo diseaseManagerUserInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bucketInfo")
    private List<ReqStorageInfo> reqStorageInfos = new ArrayList<>();

    public BucketInfo(ReqStorageInfo reqStorageInfo){
        this.bucketName = makeBucketName(reqStorageInfo).toLowerCase();
        this.bucketDesc = reqStorageInfo.getDataName();
        this.diseaseManagerUserSeq = reqStorageInfo.getDiseaseManagerUserSeq();
    }

    private String makeBucketName(ReqStorageInfo reqStorageInfo){

        // 버킷 명명규칙 : dranswer2 + 병원코드 + 질병코드 + 회원ID + UUID
        Integer agencySeq = reqStorageInfo.getDiseaseManagerUserInfo().getAgencySeq();

        return "dranswer2-"+agencySeq+"-"+
                reqStorageInfo.getDiseaseCode().name()+"-"+
                reqStorageInfo.getDiseaseManagerUserSeq()+"-"+reqStorageInfo.getReqStorageId();
    }
}
