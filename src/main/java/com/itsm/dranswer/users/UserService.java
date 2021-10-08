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
import com.itsm.dranswer.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserInfoRepo userInfoRepo;

    private final UserInfoRepoSupport userInfoRepoSupport;

    private final CustomMailSender customMailSender;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserInfoRepo userInfoRepo, UserInfoRepoSupport userInfoRepoSupport, CustomMailSender customMailSender){
        this.userInfoRepo = userInfoRepo;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepoSupport = userInfoRepoSupport;
        this.customMailSender = customMailSender;
    }

    /**
     *
     * @methodName : login
     * @date : 2021-10-08 오후 2:47
     * @author : xeroman.k
     * @param email
     * @param password
     * @return : com.itsm.dranswer.users.UserInfo
     * @throws
     * @modifyed :
     *
    **/
    @Transactional
    public UserInfo login(String email, String password) {

        UserInfo userInfo = findByEmailAndCheck(email);
        userInfo.login(passwordEncoder, email, password);
        userInfo.afterLoginSuccess();

        return userInfo;
    }

    /**
     *
     * @methodName : findByEmailAndCheck
     * @date : 2021-10-08 오후 2:47
     * @author : xeroman.k
     * @param email
     * @return : com.itsm.dranswer.users.UserInfo
     * @throws
     * @modifyed :
     *
    **/
    @Transactional
    public UserInfo findByEmailAndCheck(String email){
        UserInfo userInfo = findByEmail(email)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 이메일 입니다. " + email));

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
     * @methodName : sendCertMailForFindPw
     * @date : 2021-10-08 오후 2:47
     * @author : xeroman.k
     * @param certDto
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void sendCertMailForFindPw(CertDto certDto) throws MessagingException, IOException {

        String template = "mail/pwdCert";
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

        List<UserInfo> userInfos = userInfoRepo.findByUserNameAndUserPhoneNumber(userInfoDto.getUserName(), userInfoDto.getUserPhoneNumber());

        checkUserList(userInfos);

        return userInfos.stream().map(UserInfoDto::new).collect(Collectors.toList());

    }

    /**
     *
     * @methodName : checkUserList
     * @date : 2021-10-08 오후 2:47
     * @author : xeroman.k
     * @param userInfos
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    private void checkUserList(List<UserInfo> userInfos){
        if(userInfos.size() == 0){
            throw new IllegalArgumentException("회원정보가 존재하지 않습니다.");
        }
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
     * @date : 2021-08-06 오후 6:55
     * @author : xeroman.k
 * @param loginUserSeq
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UserInfoDto getOriginUser(Long loginUserSeq) {

        UserInfo userInfo = findUserInfo(loginUserSeq);
        Long parentSeq = userInfo.getParentUserSeq();

        if(parentSeq == null){
            return userInfo.convertDto();
        }else{
            return findUserInfo(parentSeq).convertDto();
        }



    }

    /**
     *
     * @methodName : getReqStorageUserInfo
     * @date : 2021-10-08 오후 2:47
     * @author : xeroman.k
     * @param userSeq
     * @return : com.itsm.dranswer.users.ReqUserDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqUserDto getReqStorageUserInfo(Long userSeq) {

        UserInfo userInfo = findUserInfo(userSeq);

        if(userInfo.isManager()){
            return new ReqUserDto(userInfo);
        }else{

            throw new IllegalArgumentException("질병 책임자가 아닙니다.");

//            Long managerSeq = userInfo.getParentUserSeq();
//
//            if(managerSeq == null){
//                throw new IllegalArgumentException("소속 질병책임자가 존재하지 않습니다.");
//            }
//
//            UserInfo manager = findUserInfo(managerSeq);
//
//            if(manager.isManager()){
//                return new ReqUserDto(userInfo, manager);
//            }else{
//                throw new IllegalArgumentException("소속이 존재하나, 질병책임자가 존재하지 않습니다.");
//            }
        }
    }

    /**
     *
     * @methodName : findUserInfo
     * @date : 2021-07-06 오전 11:00
     * @author : xeroman.k
 * @param userSeq
     * @return : com.itsm.dranswer.users.UserInfo
     * @throws
     * @modifyed :
     *
    **/
    public UserInfo findUserInfo(Long userSeq){
        return userInfoRepo.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원정보 입니다."));
    }

    /**
     *
     * @methodName : getUserList
     * @date : 2021-07-06 오전 11:00
     * @author : xeroman.k
     * @param joinStat
     * @param userName
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<UserInfoDto> getUserList(JoinStat joinStat, String userName, Pageable pageable) {

        return userInfoRepoSupport.searchAll(joinStat, userName, pageable);
    }

    /**
     *
     * @methodName : acceptUser
     * @date : 2021-07-06 오후 1:07
     * @author : xeroman.k
     * @param userInfoDto
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UserInfoDto acceptUser(UserInfoDto userInfoDto) {

        UserInfo userInfo = findUserInfo(userInfoDto.getUserSeq());
        userInfo.accept();

        sendAcceptMail(userInfo);

        return new UserInfoDto(userInfo, userInfo.getAgencyInfo());
    }

    /**
     *
     * @methodName : sendAcceptMail
     * @date : 2021-10-08 오후 2:47
     * @author : xeroman.k
     * @param userInfo
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void sendAcceptMail(UserInfo userInfo) {

        String template = "mail/joinAccept";
        String subject = "[닥터앤서]가입이 승인 되었습니다.";
        String[] to = {userInfo.getUserEmail()};
        Context ctx = new Context();
        ctx.setVariable("userName", userInfo.getUserName());

        try {
            customMailSender.sendMail(template, subject, to, ctx);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @methodName : changeMyInfo
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param userInfoDto
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UserInfoDto changeMyInfo(UserInfoDto userInfoDto) {
        UserInfo userInfo = this.findUserInfo(userInfoDto.getUserSeq());
        userInfo.changeMyInfo(userInfoDto);

        return userInfo.convertDto();
    }

    /**
     *
     * @methodName : changeMyPw
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param userInfoDto
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UserInfoDto changeMyPw(UserInfoDto userInfoDto) {
        UserInfo userInfo = this.findUserInfo(userInfoDto.getUserSeq());
        userInfo.changeMyPw(userInfoDto, passwordEncoder);

        return userInfo.convertDto();
    }

    /**
     *
     * @methodName : getUserDetailInfo
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param userSeq
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UserInfoDto getUserDetailInfo(Long userSeq){
        UserInfo userInfo = this.findUserInfo(userSeq);

        if(userInfo.getParentUserSeq() == null){
            return new UserInfoDto(userInfo, userInfo.getAgencyInfo());
        }else{
            UserInfo parentUserInfo = this.findUserInfo(userInfo.getParentUserSeq());
            return new UserInfoDto(userInfo, userInfo.getAgencyInfo(), parentUserInfo);
        }

    }

    /**
     *
     * @methodName : getMyUploader
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param userSeq
     * @return : java.util.List<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public List<UserInfoDto> getMyUploader(Long userSeq) {

        return userInfoRepoSupport.getUploaderList(userSeq);

    }

    /**
     *
     * @methodName : getOnlyMyUploader
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param userSeq
     * @return : java.util.List<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public List<UserInfoDto> getOnlyMyUploader(Long userSeq) {

        return userInfoRepo.findByParentUserSeqOrderByUserName(userSeq).stream().map(UserInfoDto::new).collect(Collectors.toList());

    }

    /**
     *
     * @methodName : saveMyUploader
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param userSeq
     * @param uploaders
     * @return : java.util.List<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public List<UserInfoDto> saveMyUploader(Long userSeq, List<UserInfoDto> uploaders){

        for(UserInfoDto userInfoDto : uploaders){
            UserInfo userInfo = this.findUserInfo(userInfoDto.getUserSeq());
            userInfo.matchParent(userInfoDto.getParentUserSeq());
        }

        return getMyUploader(userSeq);
    }

    /**
     *
     * @methodName : initMyManager
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param userSeq
     * @return : com.itsm.dranswer.users.UserInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UserInfoDto initMyManager(Long userSeq) {
        UserInfo userInfo = this.findUserInfo(userSeq);
        userInfo.matchParent(null);

        return userInfo.convertDto();
    }

    /**
     *
     * @methodName : checkMailAndSendCertMailForFindPw
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param certDto
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void checkMailAndSendCertMailForFindPw(CertDto certDto) throws MessagingException, IOException {

        findByEmailAndCheck(certDto.getUserEmail());
        sendCertMailForFindPw(certDto);

    }

    /**
     *
     * @methodName : checkMailAndSendCertMailForSignup
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param certDto
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void checkMailAndSendCertMailForSignup(CertDto certDto) throws MessagingException, IOException {
        UserInfo userInfo = findByEmail(certDto.getUserEmail()).orElse(null);
        if(userInfo != null){
            throw new IllegalArgumentException("사용중인 이메일 입니다.");
        }
        sendCertMail(certDto);
    }

    /**
     *
     * @methodName : getNCloudKeys
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
     * @param userSeq
     * @return : com.itsm.dranswer.users.NCloudKeyDto
     * @throws
     * @modifyed :
     *
    **/
    @Cacheable(value = "getNCloudKeys", key = "#userSeq")
    public NCloudKeyDto getNCloudKeys(Long userSeq){
        return userInfoRepoSupport.searchForNCloudKey(userSeq);
    }

    /**
     *
     * @methodName : getNCloudKey
     * @date : 2021-10-08 오후 2:48
     * @author : xeroman.k
 * @param userSeq
     * @return : com.itsm.dranswer.users.NCloudKeyDto
     * @throws
     * @modifyed :
     *
    **/
    public NCloudKeyDto getNCloudKey(Long userSeq){
        if(userSeq==null) throw new NotFoundException("접속정보가 존재하지 않습니다");

        NCloudKeyDto nCloudKeyDto = this.getNCloudKeys(userSeq);

        return nCloudKeyDto;
    }


}

