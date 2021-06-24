package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : ReqStorageRestCtrl.java
 * @date : 2021-06-24 오후 2:34
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class ReqStorageRestCtrl {

    private final StorageService storageService;

    @Autowired
    public ReqStorageRestCtrl(StorageService storageService) {
        this.storageService = storageService;
    }


    /**
     * 데이터 저장 신청 목록
     * @methodName : getReqStorageList
     * @date : 2021-06-24 오후 2:33
     * @author : xeroman.k
     * @param reqStorageStatCode
     * @param dataName
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.storage.ReqStorageInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/storage/req/list")
    public ApiResult<Page<ReqStorageInfoDto>> getReqStorageList(
            ReqStorageStat reqStorageStatCode,
            String dataName,
            Pageable pageable){

        Page<ReqStorageInfoDto> pageReqStorages =
                storageService.getReqStorageList(reqStorageStatCode, dataName, pageable);

        return success(pageReqStorages);
    }

    /**
     * 데이터 저장 신청
     * @methodName : reqStorage
     * @date : 2021-06-24 오후 2:33
     * @author : xeroman.k
     * @param reqStorageInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/storage/req")
    public ApiResult<ReqStorageInfoDto> reqStorage(ReqStorageInfoDto reqStorageInfoDto){

        return success(storageService.reqStorage(reqStorageInfoDto));
    }
}
