package com.itsm.dranswer;

import com.itsm.dranswer.users.UserService;
import com.itsm.dranswer.ncp.storage.CustomObjectStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DranswerApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomObjectStorage customObjectStorage;

//    @Test
    void contextLoads() {

        String userEmail = "";
        String userName = "";
        String inputPw = "";

//        UserInfoDto userInfoDto = new UserInfoDto(userEmail, userName, inputPw);
//
//        Throwable exception = assertThrows(ConstraintViolationException.class, () -> {
//            userService.join(userInfoDto);
//        });
//
//        System.out.println("message = "+exception.getMessage());

    }

    @Test
    void testObjectStorage(){

        final String endPoint = "https://kr.object.ncloudstorage.com";
        final String regionName = "kr-standard";
        String accessKey = "KCbWL7rOSXloPrSx7RLF";
        String secretKey = "2HC4LpXz6CiPWBWG0FeDmYAulQzgVxHtWcjmiWgq";

        customObjectStorage.getBucketList(endPoint, regionName, accessKey, secretKey);

    }

}
