package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : ReqStorageRestCtrl.java
 * @date : 2021-06-24 오후 2:34
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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
                storageService.getReqStorageList(reqStorageStatCode, dataName, null, pageable);

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
    @Secured(value = {"ROLE_MANAGER"})
    @PostMapping(value = "/storage/req")
    public ApiResult<ReqStorageInfoDto> reqStorage(@RequestBody ReqStorageInfoDto reqStorageInfoDto){

        return success(storageService.reqStorage(reqStorageInfoDto));
    }

    /**
     * 마이페이지 > 스토리지저장신청목록
     * @param loginUserInfo
     * @param reqStorageStatCode
     * @param dataName
     * @param pageable
     * @return
     */
    @GetMapping(value = "/my/management/storage/req/list")
    public ApiResult<Page<ReqStorageInfoDto>> getMyReqStorageList(
            @LoginUser LoginUserInfo loginUserInfo,
            ReqStorageStat reqStorageStatCode,
            String dataName,
            Pageable pageable){

        Page<ReqStorageInfoDto> pageReqStorages =
                storageService.getReqStorageList(reqStorageStatCode, dataName, loginUserInfo.getUserSeq(), pageable);

        return success(pageReqStorages);
    }

    /**
     * 마이페이지 > 스토리지저장신청목록 > 상세
     * @methodName : getMyReqStorageInfo
     * @date : 2021-06-25 오후 1:59
     * @author : xeroman.k
     * @param reqStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping(value = "/my/management/storage/req/{reqStorageId:.+(?<!\\.js)$}")
    public ApiResult<ReqStorageInfoDto> getMyReqStorageInfo(
            @PathVariable String reqStorageId){

        ReqStorageInfoDto reqStorageInfoDto =
                storageService.getReqStorage(reqStorageId);

        return success(reqStorageInfoDto);
    }

    /**
     * 
     * @methodName : cancelReqStorage
     * @date : 2021-06-25 오후 3:09
     * @author : xeroman.k 
     * @param loginUserInfo
     * @param reqStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_MANAGER"})
    @PostMapping(value = "/my/management/storage/req/cancel/{reqStorageId:.+(?<!\\.js)$}")
    public ApiResult<ReqStorageInfoDto> cancelReqStorage(
            @LoginUser LoginUserInfo loginUserInfo,
            @PathVariable String reqStorageId
            ){
        return success(storageService.cancelReqStorageInfo(loginUserInfo, reqStorageId));
    }

    /**
     *
     * @methodName : deleteReqStorage
     * @date : 2021-06-29 오후 4:32
     * @author : xeroman.k
     * @param loginUserInfo
     * @param reqStorageId
     * @param reqStorageInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_MANAGER"})
    @PostMapping(value = "/my/management/storage/req/delete/{reqStorageId:.+(?<!\\.js)$}")
    public ApiResult<ReqStorageInfoDto> deleteReqStorage(
            @LoginUser LoginUserInfo loginUserInfo,
            @PathVariable String reqStorageId,
            @RequestBody ReqStorageInfoDto reqStorageInfoDto
    ){
        return success(storageService.deleteReqStorageInfo(loginUserInfo, reqStorageId, reqStorageInfoDto));
    }

    /**
     * 
     * @methodName : approveReqStorage
     * @date : 2021-06-28 오전 11:28
     * @author : xeroman.k 
     * @param reqStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/req/approve/{reqStorageId:.+(?<!\\.js)$}")
    public ApiResult<ReqStorageInfoDto> approveReqStorage(
            @PathVariable String reqStorageId, @RequestBody BucketInfoDto bucketInfoDto){

        return success(storageService.approveReqStorageInfo(reqStorageId, bucketInfoDto));
    }
    
    /**
     * 
     * @methodName : deleteReqStorage
     * @date : 2021-07-08 오전 10:46
     * @author : xeroman.k 
     * @param reqStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/req/delete/{reqStorageId:.+(?<!\\.js)$}")
    public ApiResult<ReqStorageInfoDto> deleteReqStorage(
            @PathVariable String reqStorageId){

        return success(storageService.deleteReqStorageInfo(reqStorageId));
    }

    /**
     * 
     * @methodName : rejectReqStorage
     * @date : 2021-07-08 오전 10:46
     * @author : xeroman.k 
 * @param reqStorageId
 * @param reqStorageInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/req/reject/{reqStorageId:.+(?<!\\.js)$}")
    public ApiResult<ReqStorageInfoDto> rejectReqStorage(
            @PathVariable String reqStorageId,
            @RequestBody ReqStorageInfoDto reqStorageInfoDto){

        return success(storageService.rejectReqStorageInfo(reqStorageId, reqStorageInfoDto));
    }
}
