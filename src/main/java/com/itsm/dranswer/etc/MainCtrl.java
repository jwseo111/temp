package com.itsm.dranswer.etc;

import com.amazonaws.services.s3.model.Bucket;
import com.itsm.dranswer.ncp.storage.CustomObjectStorage;
import com.itsm.dranswer.security.Jwt;
import com.itsm.dranswer.users.JoinRequest;
import com.itsm.dranswer.users.UserInfo;
import com.itsm.dranswer.users.UserInfoDto;
import com.itsm.dranswer.users.UserService;
import com.itsm.dranswer.utils.ApiUtils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static com.itsm.dranswer.utils.ApiUtils.success;

@Controller
public class MainCtrl {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private CustomObjectStorage customObjectStorage;

    @GetMapping(value = "/test")
    @ResponseBody
    public ApiResult<UserInfo> test(){
        String userEmail = "kkhkykkk2@naver.com";
        String userName = "김영남";
        String inputPw = "dudghk113!";

        JoinRequest request = new JoinRequest(userEmail, userName, inputPw);

        UserInfo user = userService.join(request);

        return success(user);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/test2")
    @ResponseBody
    public ApiResult<UserInfoDto> test2(){

        UserInfoDto user = new UserInfoDto();

        return success(user);
    }

    @Secured({"ROLE_USER"})
    @GetMapping(value = "/test3")
    @ResponseBody
    public ApiResult<UserInfoDto> test3(){

        UserInfoDto user = new UserInfoDto();

        return success(user);
    }

    @GetMapping(value = "/test4")
    @ResponseBody
    public ApiResult<UserInfoDto> test4(HttpServletRequest request){

        String token = Arrays.stream(request.getCookies())
                .filter(c -> Jwt.COOKIE_NAME.equals(c.getName()))
                .findFirst().map(Cookie::getValue)
                .orElse("dummy");

        UserInfoDto user = new UserInfoDto();

        return success(user);
    }

    @GetMapping(value = "/listBuckets")
    @ResponseBody
    public ApiResult<List<Bucket>> listBuckets(HttpServletRequest request){

        final String endPoint = "https://kr.object.ncloudstorage.com";
        final String regionName = "kr-standard";
        String accessKey = "KCbWL7rOSXloPrSx7RLF";
        String secretKey = "2HC4LpXz6CiPWBWG0FeDmYAulQzgVxHtWcjmiWgq";

        List<Bucket> listBuckets = customObjectStorage.getBucketList(endPoint, regionName, accessKey, secretKey);

        return success(listBuckets);
    }

}
