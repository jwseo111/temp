package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserRestCtrl.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.Disease;
import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.errors.UnauthorizedException;
import com.itsm.dranswer.security.Jwt;
import com.itsm.dranswer.security.JwtAuthenticationToken;
import com.itsm.dranswer.utils.ApiUtils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class UserRestCtrl {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Jwt jwt;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    /**
     *
     * @methodName : UserRestCtrl
     * @date : 2021-06-23 오후 2:27
     * @author : xeroman.k
     * @param jwt
     * @param authenticationManager
     * @param userService
     * @return :
     * @throws
     * @modifyed :
     *
    **/
    @Autowired
    public UserRestCtrl(Jwt jwt,  AuthenticationManager authenticationManager, UserService userService){
        this.jwt = jwt;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    /**
     *
     * @methodName : login
     * @date : 2021-06-23 오후 2:24
     * @author : xeroman.k
     * @param request
     * @param session
     * @param response
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.LoginResult>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(path = "/login")
    public ApiResult<LoginResult> login(
            @Valid @RequestBody LoginRequest request, HttpSession session,
            HttpServletResponse response
    ) throws UnauthorizedException {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new JwtAuthenticationToken(request.getEmail(), request.getPassword())
            );
            final UserInfo user = (UserInfo) authentication.getDetails();
            final String token = user.newJwt(
                    jwt,
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toArray(String[]::new)
            );

            this.setTokenInCookie(response, token);

            return success(new LoginResult(token, user));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    /**
     *
     * @methodName : setTokenInCookie
     * @date : 2021-06-23 오후 2:24
     * @author : xeroman.k
     * @param response
     * @param token
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    private void setTokenInCookie(HttpServletResponse response,
                                  String token){
        Cookie cookie = new Cookie(Jwt.COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.MAX_VALUE);

        response.addCookie(cookie);
    }

    /**
     *
     * @methodName : check
     * @date : 2021-06-23 오후 2:24
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.config.LoginUserInfo>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/user/check")
    public ApiResult<LoginUserInfo> check(@LoginUser LoginUserInfo loginUserInfo){

        return success(loginUserInfo);
    }

    /**
     *
     * @methodName : logout
     * @date : 2021-06-23 오후 2:24
     * @author : xeroman.k
     * @param res
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<java.lang.String>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/user/logout")
    public ApiResult<String> logout(HttpServletResponse res) {
        Cookie cookie = new Cookie(Jwt.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);

        return success("logout");
    }

    /**
     * 회원가입처리
     * @methodName : join
     * @date : 2021-06-23 오후 2:05
     * @author : xeroman.k
     * @param request
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/user/join")
    public ApiResult<UserInfoDto> join(@RequestBody @Valid JoinRequest request){

        UserInfoDto user = userService.join(request);

        return success(user);
    }

    /**
     *
     * @methodName : checkMail
     * @date : 2021-07-26 오전 10:28
     * @author : xeroman.k
     * @param certDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.CertDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/user/checkAndCert/mail")
    public ApiResult<CertDto> checkMail(@RequestBody @Valid CertDto certDto) throws MessagingException, IOException {

        userService.checkMailAndSendCertMailForSignup(certDto);

        return success(certDto);
    }

    /**
     * 인증메일 발송
     * @methodName : certMail
     * @date : 2021-06-23 오후 2:23
     * @author : xeroman.k
     * @param certDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.CertDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/user/cert/mail")
    public ApiResult<CertDto> certMail(@RequestBody @Valid CertDto certDto) throws MessagingException, IOException {

        userService.sendCertMail(certDto);

        return success(certDto);
    }

    /**
     *
     * @methodName : fndPwCertMail
     * @date : 2021-07-26 오전 10:28
     * @author : xeroman.k
     * @param certDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.CertDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/user/find/pw/cert/mail")
    public ApiResult<CertDto> findPwCertMail(@RequestBody @Valid CertDto certDto) throws MessagingException, IOException {

        userService.checkMailAndSendCertMailForFindPw(certDto);

        return success(certDto);
    }

    /**
     * 사용자 연락처로 이메일 리스트를 조회
     * @methodName : findMail
     * @date : 2021-06-23 오후 2:23
     * @author : xeroman.k
     * @param userInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/user/find/mail")
    public ApiResult<List<UserInfoDto>> findMail(@RequestBody @Valid UserInfoDto userInfoDto) {
        return success(userService.findByPhoneNumber(userInfoDto));
    }

    /**
     * 비밀번호 변경처리
     * @methodName : changePw
     * @date : 2021-06-23 오후 2:24
     * @author : xeroman.k
     * @param userInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/user/change/pw")
    public ApiResult<UserInfoDto> changePw(@RequestBody @Valid UserInfoDto userInfoDto) {

        return success(userService.changePassword(userInfoDto));
    }

    /**
     * 상위 회원 객체를 리턴, 상위회원이 없을경우 자신을 리턴
     * @methodName : originUserSeq
     * @date : 2021-06-23 오후 6:14
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/user/origin/info")
    public ApiResult<UserInfoDto> originUserSeq(@LoginUser LoginUserInfo loginUserInfo){

        UserInfoDto parent = userService.getOriginUser(loginUserInfo.getUserSeq());

        return success(parent);
    }

    /**
     *
     * @methodName : getReqUser
     * @date : 2021-06-25 오전 10:53
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.ReqUserDto>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/user/req/storage")
    public ApiResult<ReqUserDto> getReqUser(@LoginUser LoginUserInfo loginUserInfo){

        ReqUserDto reqUserDto = userService.getReqStorageUserInfo(loginUserInfo.getUserSeq());

        return success(reqUserDto);
    }

    /**
     *
     * @methodName : getUserList
     * @date : 2021-07-06 오전 11:00
     * @author : xeroman.k
     * @param userName
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.users.UserInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping(value = "/user/list")
    public ApiResult<Page<UserInfoDto>> getUserList(
            UserType userType, Integer agencySeq, Disease disease,
            JoinStat joinStat,
            @RequestParam(required = true) String userName,
            Pageable pageable){

        Page<UserInfoDto> listUserInfoDto = userService.getUserList(userType, agencySeq, disease, joinStat, userName, pageable);

        return success(listUserInfoDto);
    }

    /**
     * 회원가입승인
     * @methodName : acceptUser
     * @date : 2021-07-06 오후 1:08
     * @author : xeroman.k
     * @param userInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/user/accept")
    public ApiResult<UserInfoDto> acceptUser(@RequestBody UserInfoDto userInfoDto){

        return success(userService.acceptUser(userInfoDto));
    }

    /**
     * 로그인 사용자 정보
     * @methodName : myInfo
     * @date : 2021-07-06 오후 5:44
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/user/my/info")
    public ApiResult<UserInfoDto> myInfo(@LoginUser LoginUserInfo loginUserInfo){
        return success(userService.getUserDetailInfo(loginUserInfo.getUserSeq()));
    }

    /**
     * 상세정보
     * @methodName : myInfo
     * @date : 2021-07-06 오후 5:44
     * @author : xeroman.k
     * @param userSeq
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/user/info/{userSeq:.+(?<!\\.js)$}")
    public ApiResult<UserInfoDto> myInfo(@PathVariable Long userSeq){
        return success(userService.getUserDetailInfo(userSeq));
    }

    /**
     * 상세정보 수정
     * @methodName : changeMyInfo
     * @date : 2021-07-06 오후 5:44
     * @author : xeroman.k
     * @param userInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/user/my/info")
    public ApiResult<UserInfoDto> changeMyInfo(@RequestBody UserInfoDto userInfoDto){
        return success(userService.changeMyInfo(userInfoDto));
    }

    /**
     * 비밀번호 수정
     * @methodName : changeMyPw
     * @date : 2021-07-06 오후 5:44
     * @author : xeroman.k
     * @param userInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/user/my/pw")
    public ApiResult<UserInfoDto> changeMyPw(@RequestBody UserInfoDto userInfoDto){
        return success(userService.changeMyPw(userInfoDto));
    }

    /**
     *
     * @methodName : getMyUploader
     * @date : 2021-07-12 오후 3:26
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<java.util.List<com.itsm.dranswer.users.UserInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_MANAGER"})
    @GetMapping(value = "/user/my/uploader")
    public ApiResult<List<UserInfoDto>> getMyUploader(@LoginUser LoginUserInfo loginUserInfo){
        return success(userService.getMyUploader(loginUserInfo.getUserSeq()));
    }

    /**
     *
     * @methodName : saveMyUploader
     * @date : 2021-07-12 오후 3:26
     * @author : xeroman.k
     * @param loginUserInfo
     * @param userInfoDtos
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<java.util.List<com.itsm.dranswer.users.UserInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_MANAGER"})
    @PostMapping(value = "/user/my/uploader")
    public ApiResult<List<UserInfoDto>> saveMyUploader(@LoginUser LoginUserInfo loginUserInfo,
                                                       @RequestBody List<UserInfoDto> userInfoDtos){
        return success(userService.saveMyUploader(loginUserInfo.getUserSeq(), userInfoDtos));
    }

    /**
     *
     * @methodName : initMyManager
     * @date : 2021-07-12 오후 5:07
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_UPLOADER"})
    @PostMapping(value = "/user/my/manager")
    public ApiResult<UserInfoDto> initMyManager(@LoginUser LoginUserInfo loginUserInfo){
        return success(userService.initMyManager(loginUserInfo.getUserSeq()));
    }
}
