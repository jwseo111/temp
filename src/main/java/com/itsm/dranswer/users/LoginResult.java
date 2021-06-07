package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : LoginResult.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResult {

    private String token;

    private UserInfoDto user;

    public LoginResult(String token, UserInfo user) {
        this.token = token;
        this.user = new UserInfoDto(user);
    }
}
