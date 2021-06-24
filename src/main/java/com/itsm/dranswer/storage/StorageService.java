package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : StorageService.java
 * @date : 2021-06-24 오후 2:34
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


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

    /**
     *
     * @methodName : getReqStorageList
     * @date : 2021-06-24 오후 2:32
     * @author : xeroman.k
     * @param reqStorageStatCode
     * @param dataName
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<ReqStorageInfoDto> getReqStorageList(ReqStorageStat reqStorageStatCode, String dataName, Pageable pageable) {

        return reqStorageInfoRepoSupport.searchAll(reqStorageStatCode, dataName, pageable);
    }

    /**
     *
     * @methodName : reqStorage
     * @date : 2021-06-24 오후 2:33
     * @author : xeroman.k
     * @param reqStorageInfoDto
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto reqStorage(ReqStorageInfoDto reqStorageInfoDto) {

        return null;

    }
}
