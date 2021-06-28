package com.itsm.dranswer.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenStorageInfoRepo extends JpaRepository<OpenStorageInfo, String> {

}
