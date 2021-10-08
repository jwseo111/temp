package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : NCloudVpcEnv.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
public class NCloudVpcLoginKey extends BaseEntity implements Serializable  {

    private static final long serialVersionUID = -6098692582599128272L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36) COMMENT '로그인키 ID'")
    private String loginKeyId;

    @Column(columnDefinition = "varchar(30) COMMENT 'key Name'")
    private String keyName;

    @Column(columnDefinition = "bigint COMMENT '신청회원번호'")
    private Long reqUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqUserSeq", referencedColumnName = "userSeq", insertable = false, updatable = false)
    private UserInfo reqUserInfo;

    @Column(columnDefinition = "varchar(4000) COMMENT 'private key'")
    private String privateKey;


    public NCloudVpcLoginKey(String keyName, Long userSeq, String privateKey) {
        super();
        this.keyName = keyName;
        this.reqUserSeq = userSeq;
        this.privateKey = privateKey;
    }
}
