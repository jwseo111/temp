package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.UserInfo;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UseStorageInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3963545574020782821L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36) COMMENT '사용신청Id'")
    private String useStorageId;

    @Column(columnDefinition = "varchar(10) COMMENT '사용신청상태코드'")
    @Enumerated(EnumType.STRING)
    private UseStorageStat useStorageStatCode;

    @Column(columnDefinition = "varchar(100) COMMENT '취소사유'")
    private String cancelReason;

    @Column(columnDefinition = "varchar(100) COMMENT '거절사유'")
    private String rejectReason;

    @Column(columnDefinition = "int COMMENT '사용일수 0이면 무기한'")
    private Integer usingDays;

    @Column(columnDefinition = "varchar(36) COMMENT '공개신청Id'")
    private String reqOpenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqOpenId", referencedColumnName = "reqOpenId", insertable = false, updatable = false)
    private OpenStorageInfo openStorageInfo;

    @Column(columnDefinition = "bigint COMMENT '신청회원번호'")
    private Long reqUserSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqUserSeq", referencedColumnName = "userSeq", insertable = false, updatable = false)
    private UserInfo reqUserInfo;

    @Column(columnDefinition = "datetime COMMENT '승인일시'")
    protected LocalDateTime acceptDate;

    @Column(columnDefinition = "datetime COMMENT '종료일시'")
    protected LocalDateTime endDate;

    public UseStorageInfo(UseStorageInfoDto reqUseStorageInfoDto, Long userSeq) {
        super();

        this.reqUserSeq = userSeq;
        this.useStorageStatCode = UseStorageStat.U_REQ;
        this.usingDays = reqUseStorageInfoDto.getUsingDays();
        this.reqOpenId = reqUseStorageInfoDto.getReqOpenId();
    }

    public UseStorageInfoDto convertDto() {
        return new UseStorageInfoDto(this);
    }
}
