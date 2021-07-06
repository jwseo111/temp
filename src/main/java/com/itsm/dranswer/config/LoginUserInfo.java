package com.itsm.dranswer.config;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserInfo {

    private Long userSeq;

    private String userEmail;

    private String userName;

    String[] roles;

    public boolean checkCreateUser(Long makerId) {
        return this.userSeq.equals(makerId);
    }
}
