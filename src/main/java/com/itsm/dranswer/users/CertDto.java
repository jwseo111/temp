package com.itsm.dranswer.users;

import com.itsm.dranswer.utils.StringUtil;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CertDto {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 작성해 주세요.")
    private String userEmail;

    private String certNumber;

    public CertDto(){
        this.certNumber = StringUtil.random(6);
    }

    public CertDto(String userEmail, String certNumber){
        this.userEmail = userEmail;
        this.certNumber = StringUtil.random(6);
    }

}
