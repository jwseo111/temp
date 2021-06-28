package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : ReqStorageInfoRepo.java
 * @date : 2021-06-24 오후 2:34
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReqStorageInfoRepo extends JpaRepository<ReqStorageInfo, String> {

}
