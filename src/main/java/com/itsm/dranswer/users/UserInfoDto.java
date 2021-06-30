package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserInfoDto.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private Long userSeq;

    private String userEmail;

    private String userName;

    private String inputPw;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    private String userPhoneNumber;

    private String nCloudId;

    private String nCloudAccessKey;

    private String nCloudSecretKey;

    public UserInfoDto(String userEmail, String userName) {
        this.userSeq = null;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public UserInfoDto(UserInfo source) {
        copyProperties(source, this);
    }
}
