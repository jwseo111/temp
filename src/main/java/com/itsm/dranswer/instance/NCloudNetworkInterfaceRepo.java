package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : NCloudNetworkInterfaceRepo.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NCloudNetworkInterfaceRepo extends JpaRepository<NCloudNetworkInterface, String> {
}
