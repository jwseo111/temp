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
public class PubStorageInfo extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = -269264650312946559L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36) COMMENT '공개신청Id'")
    private String reqPubId;

    @Column(columnDefinition = "varchar(100) COMMENT '공개데이터설명'")
    private String publicDataDesc;

    @Column(columnDefinition = "varchar(100) COMMENT '저장소명'")
    private String bucketName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucketName", referencedColumnName = "bucketName", insertable = false, updatable = false)
    private BucketInfo bucketInfo;

    @Column(columnDefinition = "bigint COMMENT '질병책임자회원번호'")
    private Long diseaseManagerUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq", referencedColumnName = "diseaseManagerUserSeq", insertable = false, updatable = false)
    private UserInfo diseaseManagerUserInfo;

    @Column(columnDefinition = "varchar(10) COMMENT '공개신청상태코드'")
    @Enumerated(EnumType.STRING)
    private PubStorageStat pubStorageStatCode;

    @Column(columnDefinition = "varchar(100) COMMENT '취소사유'")
    private String cancelReason;

    @Column(columnDefinition = "varchar(100) COMMENT '거절사유'")
    private String rejectReason;

}
