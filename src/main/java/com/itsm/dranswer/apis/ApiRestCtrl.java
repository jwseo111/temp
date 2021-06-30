package com.itsm.dranswer.apis;

import com.itsm.dranswer.storage.ReqStorageInfoDto;
import com.itsm.dranswer.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class ApiRestCtrl {

    private final StorageService storageService;

    @Autowired
    public ApiRestCtrl(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/apis/upload/bulk/{reqStorageId:.+(?<!\\.js)$}")
    public ApiResult<ReqStorageInfoDto> uploadBulk(
            @PathVariable String reqStorageId) {

        ReqStorageInfoDto reqStorageInfoDto =
                storageService.getReqStorage(reqStorageId);

        return success(reqStorageInfoDto);
    }
}
