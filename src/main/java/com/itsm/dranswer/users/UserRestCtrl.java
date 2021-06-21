package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserRestCtrl.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.errors.UnauthorizedException;
import com.itsm.dranswer.security.Jwt;
import com.itsm.dranswer.security.JwtAuthenticationToken;
import com.itsm.dranswer.utils.ApiUtils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class UserRestCtrl {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Jwt jwt;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @Autowired
    public UserRestCtrl(Jwt jwt,  AuthenticationManager authenticationManager, UserService userService){
        this.jwt = jwt;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

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

    private void setTokenInCookie(HttpServletResponse response,
                                  String token){
        Cookie cookie = new Cookie(Jwt.COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.MAX_VALUE);

        response.addCookie(cookie);
    }

    @GetMapping(value = "/user/check")
    public ApiResult<LoginUserInfo> check(@LoginUser LoginUserInfo loginUserInfo){

        return success(loginUserInfo);
    }

    @GetMapping(value = "/user/logout")
    public ApiResult<String> logout(HttpServletResponse res) {
        Cookie cookie = new Cookie(Jwt.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);

        return success("logout");
    }

    @GetMapping(value = "/user/join")
    public ApiResult<UserInfoDto> join(@Valid JoinRequest request){

        UserInfoDto user = userService.join(request);

        return success(user);
    }

    @GetMapping(value = "/user/cert/mail")
    public ApiResult<CertDto> certMail(@Valid CertDto certDto, String userEmail) throws MessagingException, IOException {

        userService.sendCertMail(certDto);

        return success(certDto);
    }
}
