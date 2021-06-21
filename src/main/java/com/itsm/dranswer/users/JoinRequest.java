package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : JoinRequest.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JoinRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 작성해 주세요.")
    private String userEmail;

    @NotBlank(message = "패스워드를 입력하세요.")
    private String inputPw;

    private String userRole;

    private Integer agencySeq;

    private String diseaseCode;

    @Size(message = "사용자 이름의 최대 길이를 초과 하였습니다.(max:20)", max = 20)
    @NotBlank(message = "사용자 이름을 작성해 주세요.")
    private String userName;

    private String userPhoneNumber;

    private String diseaseManagerYn;

    private String nCloudId;

    private String nCloudAccessKey;

    private String nCloudSecretKey;

    private String joinStatCode;

    private Long parentUserSeq;

}
