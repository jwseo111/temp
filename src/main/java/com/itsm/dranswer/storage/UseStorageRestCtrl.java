package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : UseStorageRestCtrl.java
 * @date : 2021-10-08 오후 2:31
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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

    /**
     *
     * @methodName : getUseStorageList
     * @date : 2021-10-08 오후 2:32
     * @author : xeroman.k
     * @param disease
     * @param keyword
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.storage.OpenStorageInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/storage/use/list")
    public ApiResult<Page<OpenStorageInfoDto>> getUseStorageList(
            Disease disease, String keyword, Pageable pageable){

        Page<OpenStorageInfoDto> pageOpenStorages =
                storageService.getOpenStorageListForUse(disease, keyword, pageable);

        return success(pageOpenStorages);
    }

    /**
     *
     * @methodName : reqUseStorage
     * @date : 2021-10-08 오후 2:32
     * @author : xeroman.k
     * @param loginUserInfo
     * @param reqUseStorageInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.UseStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/storage/use/req")
    public ApiResult<UseStorageInfoDto> reqUseStorage(
            @LoginUser LoginUserInfo loginUserInfo, @RequestBody UseStorageInfoDto reqUseStorageInfoDto
            ){

        UseStorageInfoDto useStorageInfoDto =
                storageService.reqUseStorage(loginUserInfo, reqUseStorageInfoDto);

        return success(useStorageInfoDto);
    }

    /**
     *
     * @methodName : getMyUseStorageList
     * @date : 2021-10-08 오후 2:32
     * @author : xeroman.k
     * @param loginUserInfo
     * @param useStorageStat
     * @param dataName
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.storage.UseStorageInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
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

    /**
     *
     * @methodName : getUseStorageList
     * @date : 2021-10-08 오후 2:32
     * @author : xeroman.k
     * @param useStorageStat
     * @param dataName
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.storage.UseStorageInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
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

    /**
     *
     * @methodName : getMyUseStorageInfo
     * @date : 2021-10-08 오후 2:32
     * @author : xeroman.k
     * @param useStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.UseStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping(value = "/my/management/storage/use/{useStorageId:.+(?<!\\.js)$}")
    public ApiResult<UseStorageInfoDto> getMyUseStorageInfo(
            @PathVariable String useStorageId){

        UseStorageInfoDto useStorageInfoDto =
                storageService.getUseStorage(useStorageId);

        return success(useStorageInfoDto);
    }

    /**
     *
     * @methodName : cancelUseStorageInfo
     * @date : 2021-10-08 오후 2:32
     * @author : xeroman.k
     * @param loginUserInfo
     * @param useStorageInfoDto
     * @param useStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.UseStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
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

    /**
     *
     * @methodName : rejectUseStorageInfo
     * @date : 2021-10-08 오후 2:32
     * @author : xeroman.k
     * @param loginUserInfo
     * @param useStorageInfoDto
     * @param useStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.UseStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
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

    /**
     *
     * @methodName : approveUseStorageInfo
     * @date : 2021-10-08 오후 2:32
     * @author : xeroman.k
     * @param useStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.UseStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/use/approve/{useStorageId:.+(?<!\\.js)$}")
    public ApiResult<UseStorageInfoDto> approveUseStorageInfo(
            @PathVariable String useStorageId){

        UseStorageInfoDto useStorageInfo =
                storageService.approveUseStorage(useStorageId);

        return success(useStorageInfo);
    }

    /**
     *
     * @methodName : deleteUseStorageInfo
     * @date : 2021-10-08 오후 2:46
     * @author : xeroman.k
 * @param useStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.UseStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/use/delete/{useStorageId:.+(?<!\\.js)$}")
    public ApiResult<UseStorageInfoDto> deleteUseStorageInfo(
            @PathVariable String useStorageId){

        UseStorageInfoDto useStorageInfo =
                storageService.deleteUseStorage(useStorageId);

        return success(useStorageInfo);
    }
}
