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
    @JoinColumn(name = "userSeq", referencedColumnName = "diseaseManagerUserSeq", insertable = false, updatable = false)
    private UserInfo diseaseManagerUserInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bucketInfo")
    private List<ReqStorageInfo> reqStorageInfos = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bucketInfo")
    private List<PubStorageInfo> pubStorageInfos = new ArrayList<>();
}
