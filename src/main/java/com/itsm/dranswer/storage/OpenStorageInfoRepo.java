package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : OpenStorageInfoRepo.java
 * @date : 2021-10-08 오후 2:14
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenStorageInfoRepo extends JpaRepository<OpenStorageInfo, String> {

}
