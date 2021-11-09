package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : UseStorageInfo.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.config.CustomMailSender;
import com.itsm.dranswer.instance.NCloudServerEnv;
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

    @Column(columnDefinition = "varchar(36) COMMENT '신청번호'")
    private String reqSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqSeq", referencedColumnName = "reqSeq", insertable = false, updatable = false)
    private NCloudServerEnv nCloudServerEnv;

    public UseStorageInfo(UseStorageInfoDto reqUseStorageInfoDto, OpenStorageInfo openStorageInfo, UserInfo userInfo) {
        super();

        this.reqUserSeq = userInfo.getUserSeq();
        this.reqUserInfo = userInfo;
        this.openStorageInfo = openStorageInfo;
        this.useStorageStatCode = UseStorageStat.U_REQ;
        this.usingDays = reqUseStorageInfoDto.getUsingDays();
        this.reqOpenId = reqUseStorageInfoDto.getReqOpenId();
    }

    public UseStorageInfoDto convertDto() {
        return new UseStorageInfoDto(this);
    }

    public void reqCancel(String cancelMsg) {
        this.cancelReason = cancelMsg;
        this.useStorageStatCode = UseStorageStat.U_CCL;
    }

    public void reject(String rejectMsg) {
        this.rejectReason = rejectMsg;
        this.useStorageStatCode = UseStorageStat.U_REJ;
    }

    public void approve() {
        this.acceptDate = LocalDateTime.now();

        if(this.usingDays > 0){
            this.endDate = LocalDateTime.now().plusDays(this.usingDays);
        }else{
            this.endDate = LocalDateTime.now().plusDays(9999);
        }

        this.useStorageStatCode = UseStorageStat.U_ACC;
    }

    public void delete() {
        this.useStorageStatCode = UseStorageStat.D_ACC;
    }

    public void checkUser(Long userSeq) {
        if(userSeq != this.reqUserSeq){
            throw new IllegalArgumentException("현재 사용자와 정보 소유자가 일치하지 않습니다.");
        }
    }

    public void expired(CustomMailSender customMailSender) {
        this.useStorageStatCode = UseStorageStat.D_EXP;

        String email = "ask@thelaif.com";
        String mailSubject = "[닥터앤서] 학습 데이터 사용 만료 안내";
        String title = "학습 데이터 사용 만료 안내";
        String agencyName = this.getReqUserInfo().getAgencyInfo().getAgencyName();
        String dataName = this.openStorageInfo.getOpenDataName();
        String userName = this.getReqUserInfo().getUserName();

        String msg = userName + "(" + agencyName +") 님의 " + dataName + "이(가) 만료 되었습니다.";

        customMailSender.sendExpiredAlarmMail(email, mailSubject, title, msg);
    }
}
