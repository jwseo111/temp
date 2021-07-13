package com.itsm.dranswer.storage;

import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class MyStorageRestCtrl {

    private final StorageService storageService;

    public MyStorageRestCtrl(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/my/management/storage/bucket/list")
    public ApiResult<List<ReqStorageInfoDto>> getMyStorageBucketList(
            @LoginUser LoginUserInfo loginUserInfo){

        List<ReqStorageInfoDto> reqStorageInfoDtos =
                storageService.getMyStorageBucketList(loginUserInfo);

        return success(reqStorageInfoDtos);
    }

    @GetMapping(value = "/my/management/storage/object/list/{bucketName:.+(?<!\\.js)$}")
    public ApiResult<List<S3ObjectDto>> getMyStorageObjectList(
            @LoginUser LoginUserInfo loginUserInfo,
            @PathVariable String bucketName,
            @RequestParam(required = false) String folderName){

        return success(storageService.getObjectList(loginUserInfo, bucketName, folderName));
    }
}
