package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserService.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.config.CustomMailSender;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Validated
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserInfoRepo userInfoRepo;

    private final CustomMailSender customMailSender;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserInfoRepo userInfoRepo, CustomMailSender customMailSender){
        this.userInfoRepo = userInfoRepo;
        this.passwordEncoder = passwordEncoder;
        this.customMailSender = customMailSender;
    }

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

    /**
     * 
     * @methodName : join
     * @date : 2021-06-23 오후 3:21
     * @author : xeroman.k 
     * @param request
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws 
     * @modifyed :
     *
    **/
    public UserInfoDto join(
            JoinRequest request) {
        UserInfo userInfo = new UserInfo(request, passwordEncoder);
        return this.saveUserInfo(userInfo).convertDto();
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

    /**
     * 
     * @methodName : sendCertMail
     * @date : 2021-06-23 오후 3:21
     * @author : xeroman.k 
     * @param certDto
     * @return : void
     * @throws 
     * @modifyed :
     *
    **/
    public void sendCertMail(CertDto certDto) throws MessagingException, IOException {

        String template = "mail/cert";
        String subject = "[닥터앤서]이메일 인증을 위한 인증번호가 발급되었습니다.";
        String[] to = {certDto.getUserEmail()};
        Context ctx = new Context();
        ctx.setVariable("certNumber", certDto.getCertNumber());


        customMailSender.sendMail(template, subject, to, ctx);
    }

    /**
     * 
     * @methodName : findByPhoneNumber
     * @date : 2021-06-23 오후 3:21
     * @author : xeroman.k 
     * @param userInfoDto
     * @return : java.util.List<com.itsm.dranswer.users.UserInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    public List<UserInfoDto> findByPhoneNumber(UserInfoDto userInfoDto) {

        List<UserInfo> userInfos = userInfoRepo.findByUserPhoneNumber(userInfoDto.getUserPhoneNumber());
        return userInfos.stream().map(UserInfoDto::new).collect(Collectors.toList());
    }

    /**
     * 
     * @methodName : changePassword
     * @date : 2021-06-23 오후 3:20
     * @author : xeroman.k 
     * @param userInfoDto
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws 
     * @modifyed :
     *
    **/
    public UserInfoDto changePassword(UserInfoDto userInfoDto) {
        
        UserInfo userInfo = userInfoRepo.findByUserEmail(userInfoDto.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 입니다."));

        userInfo.setPw(passwordEncoder, userInfoDto.getInputPw(), userInfoDto.getUserEmail());
        return userInfo.convertDto();
    }

    /**
     *
     * @methodName : getOriginUser
     * @date : 2021-06-23 오후 6:12
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UserInfoDto getOriginUser(LoginUserInfo loginUserInfo) {

        UserInfo userInfo = userInfoRepo.findById(loginUserInfo.getUserSeq()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원정보 입니다."));

        Long parentSeq = userInfo.getParentUserSeq();

        if(parentSeq == null){
            return userInfo.convertDto();
        }else{
            return userInfoRepo.findById(parentSeq).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원정보 입니다.")).convertDto();
        }



    }
}

