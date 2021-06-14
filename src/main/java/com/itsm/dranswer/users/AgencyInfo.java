package com.itsm.dranswer.users;

import com.itsm.dranswer.commons.BaseEntity;
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
public class AgencyInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 4221749900686609817L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(columnDefinition = "int COMMENT '기관번호'")
    private Integer agencySeq;

    @Column(columnDefinition = "varchar(50) COMMENT '기관명'")
    private String agencyName;

    @Column(columnDefinition = "varchar(20) COMMENT '대표자명'")
    private String ceoName;

    @Column(columnDefinition = "varchar(13) COMMENT '법인번호'")
    private String corporationNumber;

    @Column(columnDefinition = "varchar(10) COMMENT '기관유형코드'")
    @Enumerated(EnumType.STRING)
    private AgencyType agencyTypeCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agencyInfo")
    private List<UserInfo> userInfos = new ArrayList<>();

}
