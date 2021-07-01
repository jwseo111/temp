package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : UserInfoRepo.java
 * @date : 2021-06-07 오전 11:02
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, Long> {

    /**
     * 
     * @methodName : findByUserEmail
     * @date : 2021-06-23 오후 3:21
     * @author : xeroman.k 
     * @param userEmail
     * @return : java.util.Optional<com.itsm.dranswer.users.UserInfo>
     * @throws 
     * @modifyed :
     *
    **/
    Optional<UserInfo> findByUserEmail(String userEmail);

    /**
     * 
     * @methodName : findByUserPhoneNumber
     * @date : 2021-06-23 오후 3:21
     * @author : xeroman.k 
     * @param userPhoneNumber
     * @return : java.util.List<com.itsm.dranswer.users.UserInfo>
     * @throws 
     * @modifyed :
     *
    **/
    List<UserInfo> findByUserPhoneNumber(String userPhoneNumber);

    /**
     * 
     * @methodName : findByUserNameAndUserPhoneNumber
     * @date : 2021-07-01 오후 3:12
     * @author : xeroman.k 
     * @param userName
     * @param userPhoneNumber
     * @return : java.util.List<com.itsm.dranswer.users.UserInfo>
     * @throws 
     * @modifyed :
     *
    **/
    List<UserInfo> findByUserNameAndUserPhoneNumber(String userName, String userPhoneNumber);
}
