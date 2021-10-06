package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.Disease;
import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class UseStorageRestCtrl {

    private final StorageService storageService;

    @Autowired
    public UseStorageRestCtrl(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/storage/use/list")
    public ApiResult<Page<OpenStorageInfoDto>> getUseStorageList(
            Disease disease, String keyword, Pageable pageable){

        Page<OpenStorageInfoDto> pageOpenStorages =
                storageService.getOpenStorageListForUse(disease, keyword, pageable);

        return success(pageOpenStorages);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/storage/use/req")
    public ApiResult<UseStorageInfoDto> reqUseStorage(
            @LoginUser LoginUserInfo loginUserInfo, @RequestBody UseStorageInfoDto reqUseStorageInfoDto
            ){

        UseStorageInfoDto useStorageInfoDto =
                storageService.reqUseStorage(loginUserInfo, reqUseStorageInfoDto);

        return success(useStorageInfoDto);
    }

    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/storage/use/list")
    public ApiResult<Page<UseStorageInfoDto>> getMyUseStorageList(
            @LoginUser LoginUserInfo loginUserInfo,
            UseStorageStat useStorageStat,
            String dataName,
            Pageable pageable){

        Page<UseStorageInfoDto> useStorageInfoDtos =
                storageService.getUseStorageList(useStorageStat, dataName, loginUserInfo.getUserSeq(), pageable);

        return success(useStorageInfoDtos);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping(value = "/management/storage/use/list")
    public ApiResult<Page<UseStorageInfoDto>> getUseStorageList(
            UseStorageStat useStorageStat,
            String dataName,
            Pageable pageable){

        Page<UseStorageInfoDto> useStorageInfoDtos =
                storageService.getUseStorageList(useStorageStat, dataName, null, pageable);

        return success(useStorageInfoDtos);
    }

    @Secured(value = {"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping(value = "/my/management/storage/use/{useStorageId:.+(?<!\\.js)$}")
    public ApiResult<UseStorageInfoDto> getMyUseStorageInfo(
            @PathVariable String useStorageId){

        UseStorageInfoDto useStorageInfoDto =
                storageService.getUseStorage(useStorageId);

        return success(useStorageInfoDto);
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/my/management/storage/use/cancel/{useStorageId:.+(?<!\\.js)$}")
    public ApiResult<UseStorageInfoDto> cancelUseStorageInfo(
            @LoginUser LoginUserInfo loginUserInfo,
            @RequestBody UseStorageInfoDto useStorageInfoDto,
            @PathVariable String useStorageId){

        UseStorageInfoDto useStorageInfo =
                storageService.cancelUseStorage(useStorageId, useStorageInfoDto.getCancelReason(), loginUserInfo);

        return success(useStorageInfo);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/use/reject/{useStorageId:.+(?<!\\.js)$}")
    public ApiResult<UseStorageInfoDto> rejectUseStorageInfo(
            @LoginUser LoginUserInfo loginUserInfo,
            @RequestBody UseStorageInfoDto useStorageInfoDto,
            @PathVariable String useStorageId){

        UseStorageInfoDto useStorageInfo =
                storageService.rejectUseStorage(useStorageId, useStorageInfoDto.getRejectReason());

        return success(useStorageInfo);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/use/approve/{useStorageId:.+(?<!\\.js)$}")
    public ApiResult<UseStorageInfoDto> approveUseStorageInfo(
            @PathVariable String useStorageId){

        UseStorageInfoDto useStorageInfo =
                storageService.approveUseStorage(useStorageId);

        return success(useStorageInfo);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/use/delete/{useStorageId:.+(?<!\\.js)$}")
    public ApiResult<UseStorageInfoDto> deleteUseStorageInfo(
            @PathVariable String useStorageId){

        UseStorageInfoDto useStorageInfo =
                storageService.deleteUseStorage(useStorageId);

        return success(useStorageInfo);
    }
}
