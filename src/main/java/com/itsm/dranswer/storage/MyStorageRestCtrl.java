package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : MyStorageRestCtrl.java
 * @date : 2021-10-08 오후 2:13
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.Disease;
import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.etc.FileUploadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class MyStorageRestCtrl {

    private final StorageService storageService;

    public MyStorageRestCtrl(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 버킷리스트
     * @methodName : getMyStorageBucketList
     * @date : 2021-07-15 오후 6:09
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<java.util.List<com.itsm.dranswer.storage.ReqStorageInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/my/management/storage/bucket/list")
    public ApiResult<List<ReqStorageInfoDto>> getMyStorageBucketList(
            @LoginUser LoginUserInfo loginUserInfo){

        List<ReqStorageInfoDto> reqStorageInfoDtos =
                storageService.getMyStorageBucketList(loginUserInfo);

        return success(reqStorageInfoDtos);
    }

    /**
     * 오브젝트 리스트
     * @methodName : getMyStorageObjectList
     * @date : 2021-07-15 오후 6:09
     * @author : xeroman.k
     * @param loginUserInfo
     * @param bucketName
     * @param folderName
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<java.util.List<com.itsm.dranswer.storage.S3ObjectDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/my/management/storage/object/list/{bucketName:.+(?<!\\.js)$}")
    public ApiResult<List<S3ObjectDto>> getMyStorageObjectList(
            @LoginUser LoginUserInfo loginUserInfo,
            @PathVariable String bucketName,
            @RequestParam(required = false) String folderName){

        return success(storageService.getObjectList(loginUserInfo, bucketName, folderName));
    }

    /**
     * 파일 업로드
     * @methodName : fileUpload
     * @date : 2021-07-15 오후 6:09
     * @author : xeroman.k
     * @param loginUserInfo
     * @param request
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.etc.FileUploadResponse>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(path = "/my/management/storage/object/fileUpload")
    public ApiResult<FileUploadResponse> fileUpload(
            @LoginUser LoginUserInfo loginUserInfo,
            MultipartHttpServletRequest request
    ) throws InterruptedException {

        String bucketName = request.getParameter("bucketName");
        String folderName = request.getParameter("folderName");

        List<MultipartFile> multipartFiles = request.getFiles("multipartFile");

        return success(storageService.uploadFile(bucketName, folderName, multipartFiles, loginUserInfo));
    }

    /**
     * 오브젝트삭제
     * @methodName : deleteObject
     * @date : 2021-07-15 오후 6:09
     * @author : xeroman.k
     * @param loginUserInfo
     * @param requestObjectDtos
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<java.lang.Boolean>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(path = "/my/management/storage/object/delete")
    public ApiResult<Boolean> deleteObject(
            @LoginUser LoginUserInfo loginUserInfo,
            @RequestBody List<RequestObjectDto> requestObjectDtos
    ){

        storageService.deleteObjects(requestObjectDtos, loginUserInfo);

        return success(true);
    }

    /**
     *
     * @methodName : getStorageUsedList
     * @date : 2021-10-08 오후 2:13
     * @author : xeroman.k
     * @param agencySeq
     * @param diseaseCode
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.storage.ReqStorageInfoDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(path = "/storage/used/list")
    public ApiResult<Page<ReqStorageInfoDto>> getStorageUsedList(
            Integer agencySeq,
            Disease diseaseCode,
            Pageable pageable
    ){

        return success(storageService.getStorageUsedList(agencySeq, diseaseCode, pageable));
    }

    /**
     *
     * @methodName : getStorageUsedSummary
     * @date : 2021-10-08 오후 2:14
     * @author : xeroman.k
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<java.util.List<com.itsm.dranswer.storage.StorageSummaryDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(path = "/storage/used/summary")
    public ApiResult<List<StorageSummaryDto>> getStorageUsedSummary(
    ){

        return success(storageService.getStorageUsedSummary());
    }
}
