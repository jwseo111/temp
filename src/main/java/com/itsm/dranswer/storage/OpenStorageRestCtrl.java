package com.itsm.dranswer.storage;

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
public class OpenStorageRestCtrl {

    private final StorageService storageService;

    @Autowired
    public OpenStorageRestCtrl(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 데이터 공개 신청 목록
     * @methodName : getOpenStorageList
     * @date : 2021-07-06 오후 5:08
     * @author : xeroman.k 
     * @param openStorageStatCode
     * @param openDataName
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.storage.OpenStorageInfoDto>>
     * @throws 
     * @modifyed :
     *
    **/
    @GetMapping(value = "/storage/open/list")
    public ApiResult<Page<OpenStorageInfoDto>> getOpenStorageList(
            OpenStorageStat openStorageStatCode,
            String openDataName,
            Pageable pageable){

        Page<OpenStorageInfoDto> pageOpenStorages =
                storageService.getOpenStorageList(openStorageStatCode, openDataName, null, pageable);

        return success(pageOpenStorages);
    }

    /**
     * 데이터 공개 신청
     * @methodName : openStorage
     * @date : 2021-07-06 오후 5:08
     * @author : xeroman.k 
     * @param openStorageInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.OpenStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_MANAGER"})
    @PostMapping(value = "/storage/open")
    public ApiResult<OpenStorageInfoDto> openStorage(@RequestBody OpenStorageInfoDto openStorageInfoDto){

        return success(storageService.openStorage(openStorageInfoDto));
    }

    /**
     * 마이페이지 > 스토리지공개신청목록
     * @methodName : getMyOpenStorageList
     * @date : 2021-07-06 오후 5:08
     * @author : xeroman.k 
     * @param loginUserInfo
     * @param openStorageStatCode
     * @param openDataName
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.storage.OpenStorageInfoDto>>
     * @throws 
     * @modifyed :
     *
    **/
    @GetMapping(value = "/my/management/storage/open/list")
    public ApiResult<Page<OpenStorageInfoDto>> getMyOpenStorageList(
            @LoginUser LoginUserInfo loginUserInfo,
            OpenStorageStat openStorageStatCode,
            String openDataName,
            Pageable pageable){

        Page<OpenStorageInfoDto> pageOpenStorages =
                storageService.getOpenStorageList(openStorageStatCode, openDataName, loginUserInfo.getUserSeq(), pageable);

        return success(pageOpenStorages);
    }

    /**
     * 마이페이지 > 스토리지공개신청목록 > 상세
     * @methodName : getMyOpenStorageInfo
     * @date : 2021-07-06 오후 5:08
     * @author : xeroman.k 
     * @param openStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.OpenStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_MANAGER"})
    @GetMapping(value = "/my/management/storage/open/{openStorageId:.+(?<!\\.js)$}")
    public ApiResult<OpenStorageInfoDto> getMyOpenStorageInfo(
            @PathVariable String openStorageId){

        OpenStorageInfoDto OpenStorageInfoDto =
                storageService.getOpenStorage(openStorageId);

        return success(OpenStorageInfoDto);
    }

    /**
     * 공개취소신청
     * @methodName : cancelOpenStorage
     * @date : 2021-07-06 오후 5:08
     * @author : xeroman.k 
     * @param loginUserInfo
     * @param openStorageId
     * @param openStorageInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.OpenStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_MANAGER"})
    @PostMapping(value = "/my/management/storage/open/cancel/{openStorageId:.+(?<!\\.js)$}")
    public ApiResult<OpenStorageInfoDto> cancelOpenStorage(
            @LoginUser LoginUserInfo loginUserInfo,
            @PathVariable String openStorageId,
            @RequestBody OpenStorageInfoDto openStorageInfoDto
    ){
        return success(storageService.cancelOpenStorageInfo(loginUserInfo, openStorageId, openStorageInfoDto));
    }

    /**
     * 승인처리 (공개승인 or 취소승인)
     * @methodName : approveOpenStorage
     * @date : 2021-07-06 오후 5:09
     * @author : xeroman.k 
     * @param openStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.OpenStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/open/approve/{openStorageId:.+(?<!\\.js)$}")
    public ApiResult<OpenStorageInfoDto> approveOpenStorage(
            @PathVariable String openStorageId){

        OpenStorageInfoDto OpenStorageInfoDto =
                storageService.approveOpenStorageInfo(openStorageId);

        return success(OpenStorageInfoDto);
    }

    /**
     * 
     * @methodName : rejectOpenStorage
     * @date : 2021-07-08 오전 10:32
     * @author : xeroman.k 
 * @param openStorageId
 * @param openStorageInfoDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.OpenStorageInfoDto>
     * @throws 
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/storage/open/reject/{openStorageId:.+(?<!\\.js)$}")
    public ApiResult<OpenStorageInfoDto> rejectOpenStorage(
            @PathVariable String openStorageId,
            @RequestBody OpenStorageInfoDto openStorageInfoDto){

        OpenStorageInfoDto OpenStorageInfoDto =
                storageService.rejectOpenStorageInfo(openStorageId, openStorageInfoDto);

        return success(OpenStorageInfoDto);
    }
}
