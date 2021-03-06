package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : LoginRequest.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 작성해 주세요.")
    private String email;

    @NotBlank(message = "패스워드를 입력하세요.")
    private String password;
}
