package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : NCloudServerEnvRepo.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NCloudServerEnvRepo extends JpaRepository<NCloudServerEnv, String> {
    List<NCloudServerEnv> findByEndDateBetweenAndApproveStatus(LocalDateTime endDateStart, LocalDateTime endDateEnd, ApproveStatus created);
}
