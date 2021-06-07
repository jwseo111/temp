package com.itsm.dranswer;

import com.itsm.dranswer.users.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DranswerApplicationTests {

    @Autowired
    private UserService userService;

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

}
