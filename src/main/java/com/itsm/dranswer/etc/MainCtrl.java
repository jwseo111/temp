package com.itsm.dranswer.etc;

import com.amazonaws.services.s3.model.Bucket;
import com.itsm.dranswer.config.CustomMailSender;
import com.itsm.dranswer.ncp.storage.CustomObjectStorage;
import com.itsm.dranswer.users.JoinRequest;
import com.itsm.dranswer.users.UserInfoDto;
import com.itsm.dranswer.users.UserService;
import com.itsm.dranswer.utils.ApiUtils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.itsm.dranswer.utils.ApiUtils.success;

@Controller
public class MainCtrl {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private CustomObjectStorage customObjectStorage;

    @Autowired
    private CustomMailSender customMailSender;

    @GetMapping(value = "/makeTestUser")
    @ResponseBody
    public ApiResult<UserInfoDto> makeTestUser(){
        String userEmail = "kkhkykkk3@naver.com";
        String inputPw = "qlalfdldi";
        String userRole = "MANAGER";
        Integer agencySeq = 28;
        String diseaseCode = "THYROID_CA";
        String userName = "김영남";
        String userPhoneNumber = "01087094244";
        String diseaseManagerYn = "Y";
        String nCloudId = null;
        String nCloudAccessKey = null;
        String nCloudSecretKey = null;
        String joinStatCode = "REQUEST";
        Long parentUserSeq = null;

        JoinRequest request = new JoinRequest(userEmail, inputPw, userRole,
                agencySeq, diseaseCode, userName, userPhoneNumber,
                diseaseManagerYn, nCloudId, nCloudAccessKey,
                nCloudSecretKey, joinStatCode, parentUserSeq);

        UserInfoDto user = userService.join(request);

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

    @GetMapping("memory-status")
    @ResponseBody
    public ApiResult<MemoryStats> getMemoryStatistics() throws MessagingException, IOException {

        String template = "mail/mailtest";
        String subject = "테스트 이메일 전송";
        String[] to = {"bsmyeong@itsmart.co.kr", "com.yn.kim@gmail.com"};
        Context ctx = new Context();
        ctx.setVariable("test", "이메일 템플릿 테스트 입니다");

        customMailSender.sendMail(template, subject, to, ctx);

        MemoryStats stats = new MemoryStats();
        stats.setHeapSize(Runtime.getRuntime().totalMemory());
        stats.setHeapMaxSize(Runtime.getRuntime().maxMemory());
        stats.setHeapFreeSize(Runtime.getRuntime().freeMemory());
        return success(stats);
    }

}
