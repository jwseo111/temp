package com.itsm.dranswer.instance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NCloudServerEnvRepo extends JpaRepository<NCloudServerEnv, Long> {
}
