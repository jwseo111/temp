package com.itsm.dranswer.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    private final ReqStorageInfoRepoSupport reqStorageInfoRepoSupport;

    @Autowired
    public StorageService(ReqStorageInfoRepoSupport reqStorageInfoRepoSupport) {
        this.reqStorageInfoRepoSupport = reqStorageInfoRepoSupport;
    }

    public Page<ReqStorageInfoDto> getReqStorageList(ReqStorageStat reqStorageStatCode, String dataName, Pageable pageable) {

        return reqStorageInfoRepoSupport.searchAll(reqStorageStatCode, dataName, pageable);
    }
}
