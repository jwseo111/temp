package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : UseStorageInfoRepo.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UseStorageInfoRepo extends JpaRepository<UseStorageInfo, String> {

    List<UseStorageInfo> findByEndDateBetweenAndUseStorageStatCode(LocalDateTime endDateStart, LocalDateTime endDateEnd, UseStorageStat uAcc);
}
