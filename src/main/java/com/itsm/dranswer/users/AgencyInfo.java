package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : AgencyInfo.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "int COMMENT '기관번호'")
    private Integer agencySeq;

    @Column(columnDefinition = "varchar(50) COMMENT '기관명'")
    private String agencyName;

    @Column(columnDefinition = "varchar(20) COMMENT '대표자명'")
    private String ceoName;

    @Column(columnDefinition = "varchar(10) COMMENT '사업자번호'", unique = true)
    private String blNumber;

    @Column(columnDefinition = "varchar(10) COMMENT '기관유형코드'")
    @Enumerated(EnumType.STRING)
    private AgencyType agencyTypeCode;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agencyInfo")
    private List<UserInfo> userInfos = new ArrayList<>();

}
