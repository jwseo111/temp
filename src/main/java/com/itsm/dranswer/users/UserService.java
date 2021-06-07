package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserService.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Validated
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Transactional
    public UserInfo login(String email, String password) {

        UserInfo userInfo = findByEmail(email)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 이메일 입니다. " + email));
        userInfo.login(passwordEncoder, email, password);
        userInfo.afterLoginSuccess();
        return userInfo;
    }

    /**
     *
     * @methodName : findByEmail
     * @date : 2021-05-24 오후 4:33
     * @author : xeroman.k
     * @param email
     * @return : java.util.Optional<com.itsm.dranswer.users.UserInfo>
     * @throws
     * @modifyed :
     *
     **/
    @Transactional(readOnly = true)
    public Optional<UserInfo> findByEmail(String email) {
        checkNotNull(email, "email must be provided");

        return userInfoRepo.findByUserEmail(email);
    }


    public UserInfo join(
            @Valid
            JoinRequest request) {

        UserInfo userInfo = new UserInfo(request, passwordEncoder);
        return this.saveUserInfo(userInfo);
    }

    /**
     *
     * @methodName : saveUserInfo
     * @date : 2021-05-24 오후 4:33
     * @author : xeroman.k
     * @param userInfo
     * @return : com.itsm.dranswer.users.UserInfo
     * @throws
     * @modifyed :
     *
     **/
    @Transactional
    public UserInfo saveUserInfo(UserInfo userInfo){
        return userInfoRepo.save(userInfo);
    }

}

