package com.itsm.dranswer.apis;

/*
 * @package : com.itsm.dranswer.apis
 * @name : ApiRestCtrl.java
 * @date : 2021-10-08 오후 1:10
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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

    /**
     * 벌크 업로드 용 저장신청정보 조회
     * @methodName : uploadBulk
     * @date : 2021-10-08 오후 1:10
     * @author : xeroman.k
     * @param reqStorageId
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/apis/upload/bulk/{reqStorageId:.+(?<!\\.js)$}")
    public ApiResult<ReqStorageInfoDto> uploadBulk(
            @PathVariable String reqStorageId) {

        ReqStorageInfoDto reqStorageInfoDto =
                storageService.getReqStorage(reqStorageId);

        return success(reqStorageInfoDto);
    }
}
