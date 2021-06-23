package com.itsm.dranswer.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping(value = "/storage/req/list")
    public ApiResult<Page<ReqStorageInfoDto>> getAgencyList(
            ReqStorageStat reqStorageStatCode,
            String dataName,
            Pageable pageable){

        Page<ReqStorageInfoDto> pageReqStorages =
                storageService.getReqStorageList(reqStorageStatCode, dataName, pageable);

        return success(pageReqStorages);
    }
}
