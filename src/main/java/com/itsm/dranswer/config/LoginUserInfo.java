package com.itsm.dranswer.config;

import lombok.*;

import java.util.Arrays;

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

    public boolean isAdmin(){
        return Arrays.stream(roles).anyMatch(e -> "ROLE_ADMIN".equals(e));
    }
}
