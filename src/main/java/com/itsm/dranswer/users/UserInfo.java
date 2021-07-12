package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserInfo.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.commons.Disease;
import com.itsm.dranswer.security.Jwt;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3234070687816845610L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint COMMENT '회원번호'")
    private Long userSeq;

    @Column(unique = true, columnDefinition = "varchar(50) COMMENT '메일'")
    private String userEmail;

    @Column(columnDefinition = "varchar(200) COMMENT '로그인 비밀번호'")
    private String loginPw;

    @Column(columnDefinition = "varchar(50) COMMENT '회원구분'")
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column(columnDefinition = "int COMMENT '기관번호'")
    private Integer agencySeq;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencySeq", referencedColumnName = "agencySeq", insertable = false, updatable = false)
    private AgencyInfo agencyInfo;

    @Column(columnDefinition = "varchar(20) COMMENT '질병코드'")
    @Enumerated(EnumType.STRING)
    private Disease diseaseCode;

    @Column(columnDefinition = "varchar(20) COMMENT '담당자 이름'")
    private String userName;

    @Column(columnDefinition = "varchar(20) COMMENT '담당자 연락처'")
    private String userPhoneNumber;

    @Column(columnDefinition = "varchar(1) COMMENT '질병책임자 여부'")
    @Enumerated(EnumType.STRING)
    private IsYn diseaseManagerYn;

    @Column(columnDefinition = "varchar(50) COMMENT '네이버클라우드아이디'")
    private String nCloudId;

    @Column(columnDefinition = "varchar(50) COMMENT '네이버클라우드액세스키'")
    private String nCloudAccessKey;

    @Column(columnDefinition = "varchar(50) COMMENT '네이버클라우드시크릿키'")
    private String nCloudSecretKey;

    @Column(columnDefinition = "varchar(10) COMMENT '가입상태코드'")
    @Enumerated(EnumType.STRING)
    private JoinStat joinStatCode;

    @Column(columnDefinition = "bigint COMMENT '상위 회원번호'")
    private Long parentUserSeq;

    @Column(columnDefinition = "bigint COMMENT '로그인횟수'")
    private Long loginCount;

    @Column
    private LocalDateTime lastLoginAt;

    public UserInfo(JoinRequest request, PasswordEncoder passwordEncoder){

        this.userEmail = request.getUserEmail();
        setPw(passwordEncoder, request.getInputPw(), this.userEmail);
        this.userRole = Role.of(request.getUserRole());
        this.agencySeq = request.getAgencySeq();
        this.diseaseCode = Disease.of(request.getDiseaseCode());
        this.userName = request.getUserName();
        this.userPhoneNumber = request.getUserPhoneNumber();
        this.diseaseManagerYn = IsYn.of(request.getDiseaseManagerYn());
        this.nCloudId = request.getNCloudId();
        this.nCloudAccessKey = request.getNCloudAccessKey();
        this.nCloudSecretKey = request.getNCloudSecretKey();
        this.joinStatCode = JoinStat.REQUEST;
        this.parentUserSeq = request.getParentUserSeq();

        this.loginCount = 0L;
    }

    public void setPw(PasswordEncoder passwordEncoder, String pw, String email){
        this.loginPw = passwordEncoder.encode(pw + email);
    }

    public String newJwt(Jwt jwt, String[] roles) {
        Jwt.Claims claims = Jwt.Claims.of(userSeq, userName, roles);
        return jwt.create(claims);
    }

    public void login(PasswordEncoder passwordEncoder, String email, String credentials) {
        if (this.joinStatCode != JoinStat.ACCEPT){
            throw new IllegalArgumentException("승인되지 않은 회원 입니다.");
        }
        if (!passwordEncoder.matches(credentials+email, loginPw)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void afterLoginSuccess() {
        loginCount++;
        lastLoginAt = now();
    }

    public UserInfoDto convertDto() {
        return new UserInfoDto(this);
    }

    public boolean isManager(){
        return this.diseaseManagerYn == IsYn.Y;
    }

    public void accept() {
        this.joinStatCode = JoinStat.ACCEPT;
    }

    public void changeMyInfo(UserInfoDto userInfoDto) {
        this.userName = userInfoDto.getUserName();
        this.userPhoneNumber = userInfoDto.getUserPhoneNumber();
        this.nCloudId = userInfoDto.getNCloudId();
        this.nCloudAccessKey = userInfoDto.getNCloudAccessKey();
        this.nCloudSecretKey = userInfoDto.getNCloudSecretKey();
    }

    public void changeMyPw(UserInfoDto userInfoDto, PasswordEncoder passwordEncoder) {

        if (!passwordEncoder.matches(userInfoDto.getInputOldPw()+userEmail, loginPw)) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

        setPw(passwordEncoder, userInfoDto.getInputNewPw(), userEmail);
    }

    public void matchParent(Long userSeq) {
        this.parentUserSeq = userSeq;
    }
}
