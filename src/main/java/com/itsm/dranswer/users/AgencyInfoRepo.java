package com.itsm.dranswer.users;


/*
 * @package : com.itsm.dranswer.users
 * @name : AgencyInfoRepo.java
 * @date : 2021-06-14 오후 4:48
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyInfoRepo extends JpaRepository<AgencyInfo, Integer> {

}
