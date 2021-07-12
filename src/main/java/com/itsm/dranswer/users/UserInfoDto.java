package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserInfoDto.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.commons.Disease;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto extends BaseEntity {

    private Long userSeq;

    private String userEmail;

    private String userName;

    private String inputPw;

    private String inputOldPw;

    private String inputNewPw;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    private String userPhoneNumber;

    @Enumerated(EnumType.STRING)
    private IsYn diseaseManagerYn;

    private String nCloudId;

    private String nCloudAccessKey;

    private String nCloudSecretKey;

    @Enumerated(EnumType.STRING)
    private JoinStat joinStatCode;

    private AgencyInfoDto agencyInfo;

    @Enumerated(EnumType.STRING)
    private Disease diseaseCode;

    private Long parentUserSeq;

    public UserInfoDto(String userEmail, String userName) {
        this.userSeq = null;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public UserInfoDto(UserInfo source) {
        copyProperties(source, this);
    }

    public UserInfoDto(UserInfo userInfo, AgencyInfo agencyInfo) {
        copyProperties(userInfo, this);
        this.agencyInfo = new AgencyInfoDto(agencyInfo);
    }
}
