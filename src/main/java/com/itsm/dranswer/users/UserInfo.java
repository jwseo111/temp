package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserInfo.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.security.Jwt;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3234070687816845610L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(columnDefinition = "bigint COMMENT '회원번호'")
    private Long userSeq;

    @Column(unique = true, columnDefinition = "varchar(50) COMMENT '메일'")
    private String userEmail;

    @Column(columnDefinition = "varchar(50) COMMENT '이름'")
    private String userName;

    @Column(columnDefinition = "varchar(200) COMMENT '로그인 비밀번호'")
    private String loginPw;

    @Column(columnDefinition = "bigint COMMENT '로그인횟수'")
    private Long loginCount;

    @Column(columnDefinition = "varchar(50) COMMENT '권한'")
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column
    private LocalDateTime lastLoginAt;

    public UserInfo(JoinRequest request, PasswordEncoder passwordEncoder){
        this.userEmail = request.getUserEmail();
        this.userName = request.getUserName();
        this.loginPw = passwordEncoder.encode(request.getInputPw() + this.userEmail);
        this.loginCount = 0L;
    }

    public String newJwt(Jwt jwt, String[] roles) {
        Jwt.Claims claims = Jwt.Claims.of(userSeq, userName, roles);
        return jwt.create(claims);
    }

    public void login(PasswordEncoder passwordEncoder, String email, String credentials) {
        if (!passwordEncoder.matches(credentials+email, loginPw)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void afterLoginSuccess() {
        loginCount++;
        lastLoginAt = now();
    }
}
